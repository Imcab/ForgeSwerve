// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.List;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import frc.robot.DriveCommands.DriveCommands;
import frc.robot.DriveTrain.NetworkSwerve;
import frc.robot.DriveTrain.NetworkSwerve.SwervePathConstraints;
import lib.Forge.NetworkTableUtils.NetworkCustomPublishers.NTJoystick;
import lib.Forge.NetworkTableUtils.NetworkMultipleData.NTPublisher;
import lib.Forge.RobotState.RobotLifeCycle;

public class RobotContainer {

  private final NetworkSwerve chassis;

  private final CommandPS5Controller driver = new CommandPS5Controller(0);

  private final List<RobotLifeCycle> lifecycleSubsystems;

  public RobotContainer() {

    chassis = new NetworkSwerve(SwervePathConstraints.kNormal);

    lifecycleSubsystems = List.of(RobotState.getInstance()); //Add more subsystems here that implements RobotLifeCycle class

    NTPublisher.publish("NTControllers", "Driver1", NTJoystick.from(driver)); //publish driver joystick
    configureBindings();
  }

  private void configureBindings() {
    
    driver.circle().whileTrue(
      DriveCommands.joystickSnapAngle(
        chassis,
        ()-> -driver.getLeftY(),
        ()-> -driver.getLeftX(),
        ()-> Rotation2d.fromDegrees(0)));

    driver.cross().whileTrue(chassis.getPathFinder().toPoseCommand(new Pose2d(2.5, 2.5, Rotation2d.kZero)));

    chassis.setDefaultCommand(DriveCommands.joystickDrive(chassis, ()-> -driver.getLeftY(), ()-> -driver.getLeftX(), ()-> -driver.getRightX()));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }

  public List<RobotLifeCycle> getLifeCycle() {
    return lifecycleSubsystems;
  }
}
