package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import lib.Forge.NetworkTableUtils.MultipleData.NTPublisher;
import lib.Forge.NetworkTableUtils.NormalPublishers.NTBoolean;
import lib.Forge.NetworkTableUtils.NormalPublishers.NTDouble;
import lib.Forge.RobotState.RobotLifeCycle;

public class RobotState implements RobotLifeCycle{

    private final NTBoolean isAuto;
    private final NTBoolean isAutoDisabled;

    private final NTBoolean isTeleop;
    private final NTBoolean isTeleopDisabled;

    private final NTDouble matchTime;

    public RobotState() {

        isAuto = new NTBoolean("Robot/Auto/On");
        isAutoDisabled = new NTBoolean("Robot/Auto/Disabled");

        isTeleop = new NTBoolean("Robot/Teleop/On");
        isTeleopDisabled = new NTBoolean("Robot/Teleop/Disabled");

        matchTime = new NTDouble("Robot/MatchTime");
    }

    @Override
    public void robotPeriodic() {
        NTPublisher.updateAllSendables();

        matchTime.sendDouble(DriverStation.getMatchTime());

        isTeleop.sendBoolean(DriverStation.isTeleop());
        isTeleopDisabled.sendBoolean(!DriverStation.isTeleopEnabled());

        isAuto.sendBoolean(DriverStation.isAutonomous());
        isAutoDisabled.sendBoolean(!DriverStation.isAutonomousEnabled());
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
