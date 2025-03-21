package lib.SwerveLib.SwerveModule;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import lib.Constants.ProfileGains.PIDFGains;
import lib.Constants.ProfileGains.PIDGains;
import lib.SwerveLib.Utils.RotorConversions;
import lib.SwerveLib.Utils.SwerveModuleStateSupplier;

public abstract class SimpleSwerveModule implements Sendable{

    public static record ModuleSpecifications(double WheelRadiusInMeters, double driveGearReduction, double turnGearReduction) {
        public double WheelDiameterInMeters(){
            return WheelRadiusInMeters * 2;
        }
    }

    public static class HardwareConfiguration{
        public int drivePort = 0;
        public int turnPort = 0;
        public int encoderPort = 0;
        public double turnEncoderOffset = 0;
        public boolean driveMotorInverted = false;
        public boolean turnMotorInverted = false;
        public Translation2d modulePosition = new Translation2d();
    }

    private final PIDController driveController;
    private final SimpleMotorFeedforward driveFF;
    private final PIDController turnController;
    private final ModuleSpecifications module;
    protected final HardwareConfiguration configAccessor = new HardwareConfiguration();

    private Rotation2d angleSetpoint = null; // Setpoint for closed loop control, null for open loop
    private Double speedSetpoint = null; // Setpoint for closed loop control, null for open loop

    private final SwerveModuleStateSupplier moduleSupplier;

    private final int moduleIndex;

    private double driveOutput = 0;
    private double turnOutput = 0;

    public SimpleSwerveModule(int index, ModuleSpecifications specifications, PIDFGains drivePIDFGains, PIDGains turnPIDGains, boolean sendGainsToDashboard, boolean sendModuleToDashboard){

        this.turnController = new PIDController(turnPIDGains.kP(), turnPIDGains.kI(), turnPIDGains.kD());
        this.driveController = new PIDController(drivePIDFGains.kP(), drivePIDFGains.kI(), drivePIDFGains.kD());
        this.driveFF = new SimpleMotorFeedforward(drivePIDFGains.kS(), drivePIDFGains.kV());
        this.module = specifications;
        this.moduleIndex = index;

        if (sendGainsToDashboard) {
            SmartDashboard.putData("SwerveModuleGains: Turn" , turnPIDGains);
            SmartDashboard.putData("SwerveModuleGains: Drive", drivePIDFGains);
        }

        if (sendModuleToDashboard) {
            SmartDashboard.putData("["+ index +"]:" + "Module" , this);
        }
    
        turnController.enableContinuousInput(-Math.PI, Math.PI);

        switch (index) {
            case 0:
            FrontLeftConfiguration(configAccessor);    
                break;
            case 1:
            FrontRightConfiguration(configAccessor);
                break;
            case 2:
            BackLeftConfiguration(configAccessor);
                break;
            case 3:
            BackRightConfiguration(configAccessor);
                break;
            default:
                throw new RuntimeException("Invalid module index");
        }

        motorsConfiguration();

        moduleSupplier = new SwerveModuleStateSupplier(()-> getModuleVelocityMetersPerSecond(), ()-> getModuleAngle().getRadians());

    }

    @Override
    public void initSendable(SendableBuilder builder){
        builder.setSmartDashboardType("BulukModule");

        builder.addIntegerProperty("ModuleIndex", ()-> moduleIndex, null);
        builder.addDoubleArrayProperty("ModuleLocation",()->  new double[]{getModuleLocation().getX(), getModuleLocation().getY()}, null);
        builder.addDoubleProperty("ModulePositionMeters", ()-> getPositionMeters(), null);
        builder.addDoubleProperty("ModuleVelocityMetersPerSecond", ()-> getModuleVelocityMetersPerSecond(), null);
        builder.addDoubleProperty("ModuleAngleRadians", ()-> getModuleAngle().getRadians(), null);
        builder.addDoubleProperty("ModuleOutputDrive", ()-> driveOutput, null);
        builder.addDoubleProperty("ModuleOutputTurn", ()-> turnOutput, null);
    }

    public final SwerveModuleStateSupplier getModuleSupplier(){
        return moduleSupplier;
    }

    public final Translation2d getModuleLocation(){
        return configAccessor.modulePosition;
    }

    public abstract void motorsConfiguration();

    public abstract void FrontLeftConfiguration(HardwareConfiguration config);

    public abstract void FrontRightConfiguration(HardwareConfiguration config);

    public abstract void BackLeftConfiguration(HardwareConfiguration config);

    public abstract void BackRightConfiguration(HardwareConfiguration config);

    public abstract void setDriveVoltage(double volts);

    public abstract void setTurnVoltage(double volts);

    public abstract Rotation2d getModuleAngle();

    public abstract double getRawDriveEncoderVelocity();

    public abstract double getRawTurnEncoderVelocity();

    public final void zeroModule(){
        runSetpoint(new SwerveModuleState(0, new Rotation2d()));
    }

    public final void zeroModuleWithVelocity(double goalVelocity){
        runSetpoint(new SwerveModuleState(goalVelocity, new Rotation2d()));
    }
 
    public final void periodic(){
        if (angleSetpoint != null) {
            this.turnOutput = turnController.calculate(getModuleAngle().getRadians(), angleSetpoint.getRadians());

            setTurnVoltage(turnOutput);

            if (speedSetpoint != null) {
                double adjustSetpoint = speedSetpoint * Math.cos(turnController.getError());

                double velocityRadPerSec = adjustSetpoint / module.WheelDiameterInMeters();

                this.driveOutput = driveFF.calculate(velocityRadPerSec) + 
                driveController.calculate(
                    Units.rotationsPerMinuteToRadiansPerSecond(getRawDriveEncoderVelocity()) / module.driveGearReduction(), velocityRadPerSec);

                setDriveVoltage(driveOutput);
            }
        }
    }

    public double getModuleVelocityMetersPerSecond(){
        return RotorConversions.rotorRPMtoMetersPerSec(getRawTurnEncoderVelocity(), module.driveGearReduction(), module.WheelRadiusInMeters());
    }

    public double getPositionMeters(){
        return RotorConversions.rotorRotationsToMeters(getRawDriveEncoderVelocity(), module.driveGearReduction(), module.WheelRadiusInMeters());
    }

    public final SwerveModuleState getModuleState(){
        return new SwerveModuleState(getModuleVelocityMetersPerSecond(), getModuleAngle());
    }

    public final SwerveModulePosition getModulePosition(){
        return new SwerveModulePosition(getPositionMeters(), getModuleAngle());
    }

    public final SwerveModuleState runSetpoint(SwerveModuleState desiredState){
        desiredState.optimize(getModuleAngle());

        angleSetpoint = desiredState.angle;
        speedSetpoint = desiredState.speedMetersPerSecond;

        return desiredState;
    }
    
    public final void stopModule(){
        setDriveVoltage(0);
        setTurnVoltage(0);
        angleSetpoint = null;
        speedSetpoint = null;
    }
    
}
