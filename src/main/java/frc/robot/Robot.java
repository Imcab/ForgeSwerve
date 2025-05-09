// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import lib.Forge.NetworkTableUtils.NetworkNormalPublishers.NTString;
import lib.Forge.RobotState.RobotLifeCycle;

public class Robot extends TimedRobot {

  private final NTString autoSelected = new NTString("Robot/Auto/Selected");

  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;

  public Robot() {

    m_robotContainer = new RobotContainer();

  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();

  }

  @Override
  public void disabledInit() {
    m_robotContainer.getLifeCycle().forEach(RobotLifeCycle::disabledInit);
  }

  @Override
  public void disabledPeriodic() {
    m_robotContainer.getLifeCycle().forEach(RobotLifeCycle::disabledPeriodic);
  }

  @Override
  public void disabledExit() {
    m_robotContainer.getLifeCycle().forEach(RobotLifeCycle::disabledExit);
  }

  @Override
  public void autonomousInit() {
    m_robotContainer.getLifeCycle().forEach(RobotLifeCycle::autonomousInit);
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      autoSelected.sendString(m_autonomousCommand.getName());
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
    m_robotContainer.getLifeCycle().forEach(RobotLifeCycle::autonomousPeriodic);
  }

  @Override
  public void autonomousExit() {
    m_robotContainer.getLifeCycle().forEach(RobotLifeCycle::autonomousExit);
  }

  @Override
  public void teleopInit() {
    m_robotContainer.getLifeCycle().forEach(RobotLifeCycle::teleopInit);
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {
    m_robotContainer.getLifeCycle().forEach(RobotLifeCycle::teleopPeriodic);
  }

  @Override
  public void teleopExit() {
    m_robotContainer.getLifeCycle().forEach(RobotLifeCycle::teleopExit);
  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}
}
