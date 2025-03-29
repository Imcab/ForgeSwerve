package lib.Forge.RobotState;

public interface RobotLifeCycle {

    default void robotPeriodic() {}
    default void teleopInit() {}
    default void teleopPeriodic() {}
    default void teleopExit() {}
    default void autonomousInit() {}
    default void autonomousPeriodic() {}
    default void disabledInit() {}
    default void disabledPeriodic() {}
    default void disabledExit() {}
    default void autonomousExit() {}

}
