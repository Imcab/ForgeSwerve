// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import frc.robot.DriveCommands.DriveCommands;
import frc.robot.DriveTrain.Holonomic;
import frc.robot.DriveTrain.Holonomic.SwervePathConstraints;
import lib.NetworkTableUtils.MultipleData.NTPublisher;
import lib.NetworkTableUtils.SpecialPublishers.NTJoystick;

public class RobotContainer {

  private final Holonomic chassis;

  private final CommandPS5Controller driver = new CommandPS5Controller(0);

  public RobotContainer() {
    chassis = new Holonomic(SwervePathConstraints.kNormal);
    
    NTPublisher.publish("NTControllers", "Driver1", NTJoystick.from(driver));
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
}
