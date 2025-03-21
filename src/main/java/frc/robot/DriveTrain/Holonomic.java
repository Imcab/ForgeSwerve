package frc.robot.DriveTrain;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.PathConstraints;
import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import lib.CommandBase.Sim.RealDevice;
import lib.CommandBase.Sim.SimulatedSubsystem;
import lib.Field.FieldObject;
import lib.NetworkTableUtils.NTSubsystem.NetworkSubsystem;
import lib.NetworkTableUtils.NTSubsystem.Interfaces.AutoNetworkPublisher;
import lib.NetworkTableUtils.NormalPublishers.NTBoolean;
import lib.SwerveLib.PathFinding.PoseFinder;
import lib.SwerveLib.Utils.SwerveModuleStateSupplier;
import lib.SwerveLib.Visualizers.SwerveWidget;

public class Holonomic extends NetworkSubsystem implements SimulatedSubsystem{

    private static final double MAX_LINEAR_SPEED = Units.feetToMeters(19.0);
    private static final double TRACK_WIDTH_X = Units.inchesToMeters(28); 
    private static final double TRACK_WIDTH_Y = Units.inchesToMeters(31.7); 
    private static final double DRIVE_BASE_RADIUS =
        Math.hypot(TRACK_WIDTH_X / 2.0, TRACK_WIDTH_Y / 2.0);
    private static final double MAX_ANGULAR_SPEED = MAX_LINEAR_SPEED / DRIVE_BASE_RADIUS;

    private static final double ROBOTMASSKG = 57.45;
    private static final double ROBOTMOI = 5.16;

    private static final PathConstraints fastPathConstraints = new PathConstraints(
        4.5,
        4.0,
        Units.degreesToRadians(540),
        Units.degreesToRadians(720)
    );

    private static final PathConstraints normalPathConstraints = new PathConstraints(
        3.0,
        3.0,
        Units.degreesToRadians(540),
        Units.degreesToRadians(720)
    );

    @RealDevice
    private final AHRS navX = new AHRS(NavXComType.kMXP_SPI);

    private SwerveDriveKinematics kinematics = new SwerveDriveKinematics(getModuleLocations());

    private Rotation2d rawGyroRotation = new Rotation2d();

    private SwerveModulePosition[] lastModulePositions =
      new SwerveModulePosition[] {
        new SwerveModulePosition(),
        new SwerveModulePosition(),
        new SwerveModulePosition(),
        new SwerveModulePosition()
      };

    private SwerveDrivePoseEstimator poseEstimator =
      new SwerveDrivePoseEstimator(kinematics, rawGyroRotation, lastModulePositions, new Pose2d());

    private final PIDConstants translationPPgains; 
    private final PIDConstants rotationPPgains;
    
    private PoseFinder pathFinder;

    private SwerveModule[] modules = new SwerveModule[4];

    private final NTBoolean connection = new NTBoolean("NetworkSwerve/IsSimulated");

    public Holonomic(){
        super();
        initializeSubsystemDevices();

        if (isInSimulation()) {
            translationPPgains = new PIDConstants(5.0, 0,0);
            rotationPPgains = new PIDConstants(5.0, 0,0);
        }else{
            translationPPgains = new PIDConstants(5.5, 0, 0); 
            rotationPPgains = new PIDConstants(2.93, 0.0, 0.001);
        }

        modules[0] = new SwerveModule(0);
        modules[1] = new SwerveModule(1);
        modules[2] = new SwerveModule(2);
        modules[3] = new SwerveModule(3);

        AutoBuilder.configure(
            this::getEstimatedPosition,
            this::setPose,
            this::getChassisSpeeds,
            this::runVelocity,
            new PPHolonomicDriveController(
                translationPPgains,
                rotationPPgains,
                0.02),
            getPathPlannerConfiguration(),
            ()-> DriverStation.getAlliance().orElse(Alliance.Blue) == Alliance.Red);

        pathFinder = new PoseFinder(
            FieldObject.REEFSCAPE,
            fastPathConstraints,
            this::runVelocity,
            this::getEstimatedPosition,
            this::setPose,
            0.01,
            this);

        if (!isInSimulation()) {
            new Thread(() -> {
                try{
                    Thread.sleep(1000);
                    navX.reset();
                } catch (Exception e){
        
                }
              }).start();
        }

        SwerveModuleStateSupplier[] suppliers = new SwerveModuleStateSupplier[4];

        suppliers[0] = new SwerveModuleStateSupplier(
            ()-> modules[0].getModuleVelocity(),
            ()-> modules[0].getModuleRotation().getRadians());

        suppliers[1] = new SwerveModuleStateSupplier(
            ()-> modules[1].getModuleVelocity(),
            ()-> modules[1].getModuleRotation().getRadians());
            
        suppliers[2] = new SwerveModuleStateSupplier(
            ()-> modules[2].getModuleVelocity(),
            ()-> modules[2].getModuleRotation().getRadians());

        suppliers[3] = new SwerveModuleStateSupplier(
            ()-> modules[3].getModuleVelocity(),
            ()-> modules[3].getModuleRotation().getRadians());

        SwerveWidget.build(
            "NetworkSwerve/Elastic/SwerveWidget",
            suppliers[0],
            suppliers[1],
            suppliers[2],
            suppliers[3],
            ()-> getRotation().getRadians()
        );

        SmartDashboard.putData("NetworkSwerve/PoseFinder", pathFinder);

        connection.sendBoolean(isInSimulation());
    }

    @AutoNetworkPublisher(key = "NetworkSwerve/Modules/Locations")
    private Translation2d[] getModuleLocations(){
        return new Translation2d[] {
            new Translation2d(TRACK_WIDTH_X / 2.0, TRACK_WIDTH_Y / 2.0),
            new Translation2d(TRACK_WIDTH_X / 2.0, -TRACK_WIDTH_Y / 2.0),
            new Translation2d(-TRACK_WIDTH_X / 2.0, TRACK_WIDTH_Y / 2.0),
            new Translation2d(-TRACK_WIDTH_X / 2.0, -TRACK_WIDTH_Y / 2.0)
            };
    }

    private RobotConfig getPathPlannerConfiguration(){

      return new RobotConfig(
        ROBOTMASSKG,
        ROBOTMOI,
            new ModuleConfig(
              SwerveModule.WHEELRADIUS,
              MAX_LINEAR_SPEED,
              1.0,
              SwerveModule.NEOGearbox.
                withReduction(SwerveModule.driveMotorReduction),
              SwerveModule.driveCurrentLimit,
              1),
        getModuleLocations());
    }

    public double getAngle(){
        return Math.IEEEremainder(navX.getAngle(), 360);
    }

    public Rotation2d getnavXRotation(){
        return Rotation2d.fromDegrees(-getAngle());
    }
    
    @Override
    public void NetworkPeriodic(){

        for (var module : modules) {
            module.periodic();
        }

        if (DriverStation.isDisabled()) {
            for (var module : modules) {
              module.stopModule();
        }}

        handleSubsystemRealityLoop();

    }

    @Override
    public void RealDevicesPeriodic(){

        SwerveModulePosition[] modulePositions = getModulePositions();
        SwerveModulePosition[] moduleDeltas = new SwerveModulePosition[4];
    
        for (int moduleIndex = 0; moduleIndex < 4; moduleIndex++) {
            moduleDeltas[moduleIndex] =
                new SwerveModulePosition(
                    modulePositions[moduleIndex].distanceMeters
                        - lastModulePositions[moduleIndex].distanceMeters,
                    modulePositions[moduleIndex].angle);
            lastModulePositions[moduleIndex] = modulePositions[moduleIndex];
        }

        if (navX.isConnected() == true) {
            rawGyroRotation = getnavXRotation();
        } else {
        Twist2d twist = kinematics.toTwist2d(moduleDeltas);
        rawGyroRotation = rawGyroRotation.plus(new Rotation2d(twist.dtheta));
        }

        poseEstimator.update(rawGyroRotation, modulePositions);
        
    }

    @Override
    public void SimulationDevicesPeriodic(){
        SwerveModulePosition[] modulePositions = getModulePositions();
        SwerveModulePosition[] moduleDeltas = new SwerveModulePosition[4];
    
        for (int moduleIndex = 0; moduleIndex < 4; moduleIndex++) {
            moduleDeltas[moduleIndex] =
                new SwerveModulePosition(
                    modulePositions[moduleIndex].distanceMeters
                        - lastModulePositions[moduleIndex].distanceMeters,
                    modulePositions[moduleIndex].angle);
            lastModulePositions[moduleIndex] = modulePositions[moduleIndex];
        }

        Twist2d twist = kinematics.toTwist2d(moduleDeltas);
        rawGyroRotation = rawGyroRotation.plus(new Rotation2d(twist.dtheta));

        poseEstimator.update(rawGyroRotation, modulePositions);

    }

    public void runVelocity(ChassisSpeeds speeds) {
        // Calculate module setpoints
        ChassisSpeeds discreteSpeeds = ChassisSpeeds.discretize(speeds, 0.02);
        SwerveModuleState[] setpointStates = kinematics.toSwerveModuleStates(discreteSpeeds);
        SwerveDriveKinematics.desaturateWheelSpeeds(setpointStates, MAX_LINEAR_SPEED);

        for (int i = 0; i < 4; i++) {
            modules[i].runSetpoint(setpointStates[i]);
        }
    
    }

    public void stop(){
        runVelocity(new ChassisSpeeds());
    }

    public void modulesToHome(){
        for (int i = 0; i < 4; i++) {
              modules[i].toHome();
          }
    }

    public void stopWithX() {
        Rotation2d[] headings = new Rotation2d[4];
        for (int i = 0; i < 4; i++) {
          headings[i] = getModuleLocations()[i].getAngle();
        }
        kinematics.resetHeadings(headings);
        stop();
      }

    @AutoNetworkPublisher(key = "NetworkSwerve/Modules/ModuleStates")
    public SwerveModuleState[] getModuleStates() {
        SwerveModuleState[] states = new SwerveModuleState[4];
        for (int i = 0; i < 4; i++) {
        states[i] = modules[i].getState();
        }
        return states;
    }

    @AutoNetworkPublisher(key = "NetworkSwerve/Modules/ModulePositions")
    public SwerveModulePosition[] getModulePositions() {
        SwerveModulePosition[] states = new SwerveModulePosition[4];
        for (int i = 0; i < 4; i++) {
        states[i] = modules[i].getPosition();
        }
        return states;
    }

    @AutoNetworkPublisher(key = "NetworkSwerve/Modules/ChassisSpeeds")
    public ChassisSpeeds getChassisSpeeds(){
      return kinematics.toChassisSpeeds(getModuleStates());
    } 

    public void setPose(Pose2d pose) {
        poseEstimator.resetPosition(rawGyroRotation, getModulePositions(), pose);
    }

    @AutoNetworkPublisher(key = "NetworkSwerve/Swerve/BotPose2D")
    public Pose2d getEstimatedPosition() {
        return poseEstimator.getEstimatedPosition();
    }

    @AutoNetworkPublisher(key = "NetworkSwerve/Swerve/BotHeading")    
    public Rotation2d getRotation(){
        return getEstimatedPosition().getRotation();
    }

    public PoseFinder getPathFinder(){
        return pathFinder;
    }

    @AutoNetworkPublisher(key = "NetworkSwerve/Speeds/MaxLinear") 
    /** Returns the maximum linear speed in meters per sec. */
    public double getMaxLinearSpeedMetersPerSec() {
        return MAX_LINEAR_SPEED;
    }

    @AutoNetworkPublisher(key = "NetworkSwerve/Speeds/MaxAngular")      
    /** Returns the maximum angular speed in radians per sec. */
    public double getMaxAngularSpeedRadPerSec() {
        return MAX_ANGULAR_SPEED;
    }
}
