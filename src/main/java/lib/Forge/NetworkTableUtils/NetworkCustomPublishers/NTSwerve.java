package lib.Forge.NetworkTableUtils.NetworkCustomPublishers;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import lib.Forge.NetworkTableUtils.NetworkNormalPublishers.NTArrayData;
import lib.Forge.NetworkTableUtils.NetworkNormalPublishers.NTData;

/**
 * A utility class for publishing swerve drive data, including robot speeds, rotation, and module states, to NetworkTables.
 * This class allows sending the robot's chassis speed, rotation, and the state of each swerve module.
 */
public class NTSwerve{

    private NTData<ChassisSpeeds> robotSpeeds;
    private NTData<Rotation2d> robotRotation;
    private NTArrayData<SwerveModuleState> moduleStates;

    /**
     * Creates a new {@code NTSwerve} instance that publishes swerve drive data to the specified NetworkTables key.
     * The data includes chassis speed, robot rotation, and module states.
     *
     * @param swerveKey The base NetworkTables key used to publish the swerve data. Subkeys will be generated 
     *                  for chassis speeds, robot rotation, and module states.
     */
    public NTSwerve(String swerveKey){
        this.robotSpeeds = new NTData<>(swerveKey +"/ ChassisSpeeds", ChassisSpeeds.struct);
        this.robotRotation = new NTData<>(swerveKey + "/ RobotRotation", Rotation2d.struct);
        this.moduleStates = new NTArrayData<>(swerveKey + "/ ModuleStates", SwerveModuleState.struct);
    }

    /**
     * Publishes the current swerve drive data to NetworkTables.
     * The data includes the robot's chassis speeds, rotation, and the states of all swerve modules.
     * 
     * @param currentRobotSpeeds The current {@link ChassisSpeeds} representing the robot's linear and angular velocities.
     * @param robotRotation2D The current {@link Rotation2d} representing the robot's orientation.
     * @param currentModuleStates The current array of {@link SwerveModuleState} representing the state of each swerve module.
     */
    public void sendSwerve(ChassisSpeeds currentRobotSpeeds, Rotation2d robotRotation2D, SwerveModuleState[] currentModuleStates){
        robotSpeeds.sendData(currentRobotSpeeds);
        robotRotation.sendData(robotRotation2D);
        moduleStates.sendData(currentModuleStates);
    }

}
