package frc.robot.DriveTrain;

import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import lib.CommandBase.Sim.RealDevice;
import lib.CommandBase.Sim.SimulatedDevice;
import lib.CommandBase.Sim.SimulatedSubsystem;

public class SwerveModule extends SubsystemBase implements SimulatedSubsystem{

    public static final DCMotor driveGearbox = DCMotor.getNEO(1);
    public static final DCMotor turnGearbox = DCMotor.getNEO(1);
    private static final double WHEELRADIUS = Units.inchesToMeters(2.0);
    public static final double driveMotorReduction = 5.36;
    public static final double turnMotorReduction =  18.75;

    @RealDevice
    private SparkMax driveSparkMax;
    @RealDevice
    private SparkMax turnSparkMax;
    @RealDevice
    private CANcoder absoluteEncoder;

    @RealDevice
    private RelativeEncoder enc_drive;
    @RealDevice
    private RelativeEncoder enc_turn;

    @RealDevice
    private SparkMaxConfig config_drive = new SparkMaxConfig();
    @RealDevice
    private SparkMaxConfig config_turn = new SparkMaxConfig();

    @SimulatedDevice
    private DCMotorSim driveSim =
    new DCMotorSim(
        LinearSystemId.createDCMotorSystem(driveGearbox, 0.025, driveMotorReduction),
        driveGearbox);
    
    @SimulatedDevice
    private DCMotorSim turnSim =
    new DCMotorSim(
        LinearSystemId.createDCMotorSystem(turnGearbox, 0.004, turnMotorReduction),
        turnGearbox);

    private final PIDController drivePID;
    private final PIDController turnPID;
    private final SimpleMotorFeedforward driveFeedforward;
    private Rotation2d angleSetpoint = null;
    private Double speedSetpoint = null;

    private double Offset;

    private boolean isDriveMotorInverted;
    private boolean isTurnMotorInverted;

    private Rotation2d moduleAngle = new Rotation2d();
    private double driveVelocity = 0;

    private double driveVoltage;
    private double turnVoltage;

    public SwerveModule(int index){

        initializeSubsystemDevices();
   
        //Use real Configuration
        if (!isInSimulation()) {
            drivePID = new PIDController(0.05, 0, 0);
            driveFeedforward = new SimpleMotorFeedforward(0.1, 0.08);
            turnPID = new PIDController(5.0, 0,0);

            createSparks(index);

        }else{
        //Use sim Configuration
            drivePID = new PIDController(0.05, 0,0);
            driveFeedforward = new SimpleMotorFeedforward(0, 0.0789); 
            turnPID = new PIDController(8.0, 0,0);
        }

        turnPID.enableContinuousInput(-Math.PI, Math.PI);

    }

    private void createSparks(int index){
        switch (index) {
            case 0:
              driveSparkMax = new SparkMax(DriveConstants.frontLeft.DrivePort, MotorType.kBrushless);
              turnSparkMax = new SparkMax(DriveConstants.frontLeft.TurnPort, MotorType.kBrushless);
              absoluteEncoder = new CANcoder(DriveConstants.frontLeft.EncPort);
              isDriveMotorInverted = DriveConstants.frontLeft.DrivemotorReversed;
              isTurnMotorInverted = DriveConstants.frontLeft.TurnmotorReversed;
              Offset = DriveConstants.frontLeft.offset;
              
              break;
            case 1:
              driveSparkMax = new SparkMax(DriveConstants.frontRight.DrivePort, MotorType.kBrushless);
              turnSparkMax = new SparkMax(DriveConstants.frontRight.TurnPort, MotorType.kBrushless);
              absoluteEncoder = new CANcoder(DriveConstants.frontRight.EncPort);
              isDriveMotorInverted = DriveConstants.frontRight.DrivemotorReversed;
              isTurnMotorInverted = DriveConstants.frontRight.TurnmotorReversed;
              Offset = DriveConstants.frontRight.offset;
              
  
              break;
            case 2:
              driveSparkMax = new SparkMax(DriveConstants.backLeft.DrivePort, MotorType.kBrushless);
              turnSparkMax = new SparkMax(DriveConstants.backLeft.TurnPort, MotorType.kBrushless);
              absoluteEncoder = new CANcoder(DriveConstants.backLeft.EncPort);
              isDriveMotorInverted = DriveConstants.backLeft.DrivemotorReversed;
              isTurnMotorInverted = DriveConstants.backLeft.TurnmotorReversed;
              Offset = DriveConstants.backLeft.offset;
              
              break;
            case 3:
              driveSparkMax = new SparkMax(DriveConstants.backRight.DrivePort, MotorType.kBrushless);
              turnSparkMax = new SparkMax(DriveConstants.backRight.TurnPort, MotorType.kBrushless);
              absoluteEncoder = new CANcoder(DriveConstants.backRight.EncPort);
              isDriveMotorInverted = DriveConstants.backRight.DrivemotorReversed;
              isTurnMotorInverted = DriveConstants.backRight.TurnmotorReversed;
              Offset = DriveConstants.backRight.offset;
              
              break;
            default:
              throw new RuntimeException("Invalid module index");
          }

        driveSparkMax.setCANTimeout(250);
        turnSparkMax.setCANTimeout(250);

        enc_drive = driveSparkMax.getEncoder();

        enc_drive.setPosition(0.0);

        enc_turn = turnSparkMax.getEncoder();

          config_drive
        .inverted(isDriveMotorInverted)
        .idleMode(IdleMode.kBrake)
        .smartCurrentLimit(43)
        .voltageCompensation(12);
  
        config_drive.encoder
        .uvwAverageDepth(2)
        .uvwMeasurementPeriod(10);
  
        driveSparkMax.configure(config_drive, com.revrobotics.spark.SparkBase.ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        config_turn
        .inverted(isTurnMotorInverted)
        .idleMode(IdleMode.kBrake)
        .smartCurrentLimit(20)
        .voltageCompensation(12);
  
        config_turn.encoder
        .uvwAverageDepth(2)
        .uvwMeasurementPeriod(10);

        turnSparkMax.configure(config_turn, com.revrobotics.spark.SparkBase.ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    }

    //Main Loop
    @Override
    public void periodic(){
        handleSubsystemRealityLoop();

        if (angleSetpoint != null) {
            setTurnVolts(turnPID.calculate(getModuleRotation().getRadians(), angleSetpoint.getRadians()));

            if (speedSetpoint != null) {
                double adjustSpeedSetpoint = speedSetpoint * Math.cos(turnPID.getError());
                double velocityRadPerSec = adjustSpeedSetpoint / WHEELRADIUS;

                setDriveVolts(driveFeedforward.calculate(velocityRadPerSec) + 
                drivePID.calculate(
                    Units.rotationsPerMinuteToRadiansPerSecond(driveVelocity) / driveMotorReduction, velocityRadPerSec));
            }
        }
    }

    @Override
    public void SimulationDevicesPeriodic(){

        driveSim.update(0.02);
        turnSim.update(0.02);

        this.moduleAngle = new Rotation2d(turnSim.getAngularPositionRad());

        this.driveVelocity = driveSim.getAngularVelocityRadPerSec();

    }

    @Override
    public void RealDevicesPeriodic(){
        this.moduleAngle = Rotation2d.fromRotations(absoluteEncoder.getAbsolutePosition().getValueAsDouble() - Offset);
        this.driveVelocity = Units.rotationsPerMinuteToRadiansPerSecond(enc_drive.getVelocity());
    }

    public void setDriveVolts(double volts){

        this.driveVoltage = volts;

        if (isInSimulation()) {
            driveSim.setInputVoltage(MathUtil.clamp(volts, -12, 12));
        }else{
            driveSparkMax.setVoltage(volts);
        }
 
    }

    public void setTurnVolts(double volts){

        this.turnVoltage = volts;

        if (isInSimulation()) {
            turnSim.setInputVoltage(MathUtil.clamp(volts, -12, 12));
        }else{
            turnSparkMax.setVoltage(volts);
        }
    }

    public double getDriveModuleVoltage(){
        return driveVoltage;
    }

    public double getTurnModuleVoltage(){
        return turnVoltage;
    }

    public double getModuleVelocity(){
        return (driveVelocity * WHEELRADIUS) / driveMotorReduction;
    }

    public double getDrivePositionMeters(){

        double position = !isInSimulation() ? enc_drive.getPosition() : Units.radiansToRotations(driveSim.getAngularPositionRad());

        return Units.rotationsToRadians(
          position / driveMotorReduction)
           
          * WHEELRADIUS;
      }

    public Rotation2d getModuleRotation(){
        return moduleAngle;
    }

    public void runSetpoint(SwerveModuleState desiredState){
        desiredState.optimize(getModuleRotation());

        desiredState.cosineScale(getModuleRotation());

        angleSetpoint = desiredState.angle;
        speedSetpoint = desiredState.speedMetersPerSecond;

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
        setDriveVolts(0);
        setTurnVolts(0);
        angleSetpoint = null;
        speedSetpoint = null;
    }
}
