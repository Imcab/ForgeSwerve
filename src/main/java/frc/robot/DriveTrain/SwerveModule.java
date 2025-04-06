package frc.robot.DriveTrain;

import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import lib.Forge.Math.Constants.ProfileGains.PIDGains;
import lib.Forge.Math.Constants.ProfileGains.SimpleFeedForwardGains;
import lib.Forge.Math.Controllers.Control.PIDControl;
import lib.Forge.REV.SparkMax.ForgeSparkMax;
import lib.Forge.Sim.RealDevice;
import lib.Forge.Sim.SimulatedDevice;
import lib.Forge.Sim.SimulatedSubsystem;

public class SwerveModule implements SimulatedSubsystem{

    public static final int driveCurrentLimit = 50;
    public static final int turnCurrentLimit = 20;
    public static final DCMotor NEOGearbox = DCMotor.getNEO(1);
    public static final double WHEELRADIUS = Units.inchesToMeters(2.0);
    public static final double driveMotorReduction = 5.36;
    public static final double turnMotorReduction =  18.75;

    @RealDevice
    private ForgeSparkMax driveSparkMax;
    @RealDevice
    private ForgeSparkMax turnSparkMax;
    @RealDevice
    private CANcoder absoluteEncoder;

    @SimulatedDevice
    private DCMotorSim driveSim =
    new DCMotorSim(
        LinearSystemId.createDCMotorSystem(NEOGearbox, 0.025, driveMotorReduction),
        NEOGearbox);
    
    @SimulatedDevice
    private DCMotorSim turnSim =
    new DCMotorSim(
        LinearSystemId.createDCMotorSystem(NEOGearbox, 0.004, turnMotorReduction),
        NEOGearbox);

    private final PIDControl drivePID;
    private final PIDControl turnPID;

    private final SimpleFeedForwardGains driveFFGains;
    private final PIDGains drivePIDGains;
    private final PIDGains turnPIDGains;

    private Rotation2d angleSetpoint = null;
    private Double speedSetpoint = null;

    private double ffVolts = 0;

    private double driveVelocity;

    private double driveVoltage;
    private double turnVoltage;

    private double Offset;

    private boolean isDriveMotorInverted;
    private boolean isTurnMotorInverted;

    private Rotation2d moduleAngle = new Rotation2d();
    
    public SwerveModule(int index){
   
        //Use real Configuration
        if (!isInSimulation()) {
            this.turnPIDGains = new PIDGains(5.0, 0,0);
            this.drivePIDGains = new PIDGains(0.05, 0, 0);
            this.driveFFGains = new SimpleFeedForwardGains(0.1, 0.08, 0);
            
            createSparks(index);

        }else{
        //Use sim Configuration
            this.turnPIDGains = new PIDGains(8.0, 0,0);
            this.drivePIDGains = new PIDGains(0.05, 0, 0);
            this.driveFFGains = new SimpleFeedForwardGains(0, 0.0789, 0);

            
        }

        drivePID = new PIDControl(drivePIDGains);
        turnPID = new PIDControl(turnPIDGains);

        turnPID.continuousInput(-Math.PI, Math.PI);

        initializeSubsystemDevices("NetworkSwerve/Devices/Modules");

    }

    //Main Loop
    public void periodic(){
        handleSubsystemRealityLoop();

        if (angleSetpoint != null) {
            this.turnVoltage = turnPID.calculate(moduleAngle.getRadians()).getOutput();

            if (speedSetpoint != null) {
                this.driveVoltage = drivePID.calculate(driveVelocity).plus(()-> ffVolts).getOutput();
            }else{
                drivePID.reset();
            }
        }else{
            turnPID.reset();
        }
    }

    @Override
    public void SimulationDevicesPeriodic(){

        this.moduleAngle = new Rotation2d(turnSim.getAngularPositionRad());
        this.driveVelocity = driveSim.getAngularVelocityRadPerSec();

        driveSim.setInputVoltage(MathUtil.clamp(driveVoltage, -12.0, 12.0));
        turnSim.setInputVoltage(MathUtil.clamp(turnVoltage, -12.0, 12.0));

        driveSim.update(0.02);
        turnSim.update(0.02);
    }

    @Override
    public void RealDevicesPeriodic(){

        this.moduleAngle = 
        absoluteEncoder.isConnected() ? 
            Rotation2d.fromRotations(absoluteEncoder.getAbsolutePosition().getValueAsDouble() - Offset) : 
            Rotation2d.fromRotations(turnSparkMax.getPosition().withReduction(turnMotorReduction).getRead());

        this.driveVelocity = driveSparkMax.getVelocity().toRadiansPerSecond().getRead();

        driveSparkMax.setVoltage(driveVoltage);
        turnSparkMax.setVoltage(turnVoltage);

    }

    public void setDriveVelocity(double velocity){
        this.ffVolts = driveFFGains.kS() * Math.signum(velocity) + driveFFGains.kV() * velocity;
        drivePID.setSetpoint(velocity);
    }

    public void setTurnPos(Rotation2d rot){
        turnPID.setSetpoint(rot.getRadians());
    }

    public void setDriveOpenLoop(double voltage){
        this.speedSetpoint = null;
        this.driveVoltage = voltage;
    }

    public void setTurnOpenLoop(double voltage){
        this.angleSetpoint = null;
        this.turnVoltage = voltage;
    }

    public double getDriveModuleVoltage(){
        return driveVoltage;
    }

    public double getTurnModuleVoltage(){
        return turnVoltage;
    }

    public double getModuleVelocity(){
        return driveVelocity * WHEELRADIUS;
    }

    public double getFFCharacterizationVelocity() {
        return driveVelocity;
      }

    public double getDrivePositionMeters(){

        double position = isInSimulation() ? driveSim.getAngularPositionRad() : driveSparkMax.getPosition().toRadians().getRead();

        return position * WHEELRADIUS;
      }

    public Rotation2d getModuleRotation(){
        return moduleAngle;
    }

    public void runSetpoint(SwerveModuleState desiredState){
        desiredState.optimize(getModuleRotation());

        desiredState.cosineScale(getModuleRotation());

        angleSetpoint = desiredState.angle;
        speedSetpoint = desiredState.speedMetersPerSecond;

        setTurnPos(angleSetpoint);
        setDriveVelocity(speedSetpoint / WHEELRADIUS);

    }

    public void runCharacterization(double output){
        setDriveOpenLoop(output);
        setTurnPos(new Rotation2d(0));
    }

    public void toHome(){
        runSetpoint(new SwerveModuleState(0, new Rotation2d()));
    }

    public SwerveModulePosition getPosition(){
        return new SwerveModulePosition(getDrivePositionMeters(), getModuleRotation());
    }

    public SwerveModuleState getState(){
        return new SwerveModuleState(getModuleVelocity(), getModuleRotation());
    }

    public void stopModule(){   
        setTurnOpenLoop(0);
        setDriveOpenLoop(0);
    }

    private void createSparks(int index){
        switch (index) {
            case 0:
              driveSparkMax = new ForgeSparkMax(DriveConstants.frontLeft.DrivePort);
              turnSparkMax = new ForgeSparkMax(DriveConstants.frontLeft.TurnPort);
              absoluteEncoder = new CANcoder(DriveConstants.frontLeft.EncPort);
              isDriveMotorInverted = DriveConstants.frontLeft.DrivemotorReversed;
              isTurnMotorInverted = DriveConstants.frontLeft.TurnmotorReversed;
              Offset = DriveConstants.frontLeft.offset;
              
              break;
            case 1:
              driveSparkMax = new ForgeSparkMax(DriveConstants.frontRight.DrivePort);
              turnSparkMax = new ForgeSparkMax(DriveConstants.frontRight.TurnPort);
              absoluteEncoder = new CANcoder(DriveConstants.frontRight.EncPort);
              isDriveMotorInverted = DriveConstants.frontRight.DrivemotorReversed;
              isTurnMotorInverted = DriveConstants.frontRight.TurnmotorReversed;
              Offset = DriveConstants.frontRight.offset;
              
  
              break;
            case 2:
              driveSparkMax = new ForgeSparkMax(DriveConstants.backLeft.DrivePort);
              turnSparkMax = new ForgeSparkMax(DriveConstants.backLeft.TurnPort);
              absoluteEncoder = new CANcoder(DriveConstants.backLeft.EncPort);
              isDriveMotorInverted = DriveConstants.backLeft.DrivemotorReversed;
              isTurnMotorInverted = DriveConstants.backLeft.TurnmotorReversed;
              Offset = DriveConstants.backLeft.offset;
              
              break;
            case 3:
              driveSparkMax = new ForgeSparkMax(DriveConstants.backRight.DrivePort);
              turnSparkMax = new ForgeSparkMax(DriveConstants.backRight.TurnPort);
              absoluteEncoder = new CANcoder(DriveConstants.backRight.EncPort);
              isDriveMotorInverted = DriveConstants.backRight.DrivemotorReversed;
              isTurnMotorInverted = DriveConstants.backRight.TurnmotorReversed;
              Offset = DriveConstants.backRight.offset;
              
              break;
            default:
              throw new RuntimeException("Invalid module index");
          }

        driveSparkMax.flashConfiguration(
            isDriveMotorInverted,
            IdleMode.kBrake,
            43,
            true);
        
        turnSparkMax.flashConfiguration(
            isTurnMotorInverted,
            IdleMode.kBrake,
            20,
            true);

    }
}
