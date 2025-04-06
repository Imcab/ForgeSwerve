package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import lib.Forge.NetworkTableUtils.NetworkMultipleData.NTPublisher;
import lib.Forge.NetworkTableUtils.NetworkNormalPublishers.NTBoolean;
import lib.Forge.NetworkTableUtils.NetworkNormalPublishers.NTDouble;
import lib.Forge.RobotState.RobotLifeCycle;

public final class RobotState extends SubsystemBase implements RobotLifeCycle{

    private final NTBoolean isAuto;
    private final NTBoolean isAutoDisabled;

    private final NTBoolean isTeleop;
    private final NTBoolean isTeleopDisabled;

    private final NTDouble matchTime;

    private final Alert robotMode = new Alert("Robot is being simulated", AlertType.kInfo);

    private static RobotState instance;

    private RobotState() {

        robotMode.set(RobotBase.isSimulation());
            
        isAuto = new NTBoolean("Robot/Auto/On");
        isAutoDisabled = new NTBoolean("Robot/Auto/Disabled");

        isTeleop = new NTBoolean("Robot/Teleop/On");
        isTeleopDisabled = new NTBoolean("Robot/Teleop/Disabled");

        matchTime = new NTDouble("Robot/MatchTime");
    }

    public static synchronized RobotState getInstance(){

        if (instance == null) {
            instance = new RobotState();
        }
        return instance;
    }

    @Override
    public void periodic(){
        NTPublisher.updateAllSendables();

        matchTime.sendDouble(DriverStation.getMatchTime());

        isTeleop.sendBoolean(DriverStation.isTeleop());
        isTeleopDisabled.sendBoolean(!DriverStation.isTeleopEnabled());

        isAuto.sendBoolean(DriverStation.isAutonomous());
        isAutoDisabled.sendBoolean(!DriverStation.isAutonomousEnabled());
    }

    public Pose2d robotPosition(){
        return NTPublisher.retrieve("NetworkSwerve", "Odometry/BotPose2D", Pose2d.kZero);
    }

    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}

    @Override
    public void disabledExit() {}

    @Override
    public void autonomousInit() {}

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void autonomousExit() {}

    @Override
    public void teleopInit() {}

    @Override
    public void teleopPeriodic() {}

    @Override
    public void teleopExit() {}


}
