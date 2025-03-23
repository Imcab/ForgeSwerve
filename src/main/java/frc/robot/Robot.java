// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import lib.NetworkTableUtils.MultipleData.NTPublisher;
import lib.NetworkTableUtils.NormalPublishers.NTBoolean;
import lib.NetworkTableUtils.NormalPublishers.NTDouble;
import lib.NetworkTableUtils.NormalPublishers.NTString;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;

  private final NTString autoSelected = new NTString("Robot/Auto/Selected");
  private final NTBoolean isAuto = new NTBoolean("Robot/Auto/On");
  private final NTBoolean isAutoDisabled = new NTBoolean("Robot/Auto/Disabled");

  private final NTBoolean isTeleop = new NTBoolean("Robot/Teleop/On");
  private final NTBoolean isTeleopDisabled = new NTBoolean("Robot/Teleop/Disabled");

  private final NTDouble matchTime = new NTDouble("Robot/MatchTime");
  
  public Robot() {
    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {
    NTPublisher.updateAllSendables();
    CommandScheduler.getInstance().run();

    matchTime.sendDouble(DriverStation.getMatchTime());

    isTeleop.sendBoolean(isTeleop());
    isTeleopDisabled.sendBoolean(!isTeleopEnabled());

    isAuto.sendBoolean(isAutonomous());
    isAutoDisabled.sendBoolean(!isAutonomousEnabled());

  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      autoSelected.sendString(m_autonomousCommand.getName());
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}
}
