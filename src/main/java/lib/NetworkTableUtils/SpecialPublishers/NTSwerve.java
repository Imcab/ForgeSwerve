package lib.NetworkTableUtils.SpecialPublishers;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import lib.NetworkTableUtils.NormalPublishers.NTArrayData;
import lib.NetworkTableUtils.NormalPublishers.NTData;

public class NTSwerve{

    private NTData<ChassisSpeeds> robotSpeeds;
    private NTData<Rotation2d> robotRotation;
    private NTArrayData<SwerveModuleState> moduleStates;

    public NTSwerve(String swerveKey){
        this.robotSpeeds = new NTData<>(swerveKey +"/ ChassisSpeeds", ChassisSpeeds.struct);
        this.robotRotation = new NTData<>(swerveKey + "/ RobotRotation", Rotation2d.struct);
        this.moduleStates = new NTArrayData<>(swerveKey + "/ ModuleStates", SwerveModuleState.struct);
    }

    public void sendSwerve(ChassisSpeeds currentRobotSpeeds, Rotation2d robotRotation2D, SwerveModuleState[] currentModuleStates){
        robotSpeeds.sendData(currentRobotSpeeds);
        robotRotation.sendData(robotRotation2D);
        moduleStates.sendData(currentModuleStates);
    }

}
