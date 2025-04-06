package lib.Forge.NetworkTableUtils.NetworkSubsystem.Interfaces.Functional;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.util.Color;
import lib.Forge.NetworkTableUtils.NetworkMultipleData.NTPublisher;

/**
 * A supplier of values.
 *
 * <p>This is a functional interface whose functional method is {@link #get()}.</p>
 */
@FunctionalInterface
public interface NetworkSupplier <T>{
    /**
     * Gets a value.
     *
     * @return a value
     */
    T get();

    /**
     * Publishes the value to NetworkTables.
     *
     * @param table the table to publish to
     * @param key the key to publish to
     */
    default void publish(String table, String key){


        if (get().getClass() == Pose2d.class) {
            NTPublisher.publish(table, key, (Pose2d)get());
        }
        else if(get().getClass() == Pose3d.class){
            NTPublisher.publish(table, key, (Pose3d)get());
        }
        else if(get().getClass() == Rotation2d.class){
            NTPublisher.publish(table, key, (Rotation2d)get());
        }
        else if(get().getClass() == Rotation3d.class){
            NTPublisher.publish(table, key, (Rotation3d)get());
        }
        else if(get().getClass() == Translation2d.class){
            NTPublisher.publish(table, key, (Translation2d)get());
        }
        else if(get().getClass() == Translation3d.class){
            NTPublisher.publish(table, key, (Translation3d)get());
        }
        else if(get().getClass() == Transform2d.class){
            NTPublisher.publish(table, key, (Transform2d)get());
        }
        else if(get().getClass() == Transform3d.class){
            NTPublisher.publish(table, key, (Transform3d)get());
        }
        else if(get().getClass() == ChassisSpeeds.class){
            NTPublisher.publish(table, key, (ChassisSpeeds)get());
        }
        else if(get().getClass() == SwerveModuleState.class){
            NTPublisher.publish(table, key, (SwerveModuleState)get());
        }
        else if(get().getClass() == SwerveModulePosition.class){
            NTPublisher.publish(table, key, (SwerveModuleState)get());
        }
        else if(get().getClass() == Color.class){
            NTPublisher.publish(table, key, (Color)get());
        }
        else if(get().getClass() == String.class){
            NTPublisher.publish(table, key, (String)get());
        }
        else if(get().getClass() == Boolean.class || get().getClass() == boolean.class){
            NTPublisher.publish(table, key, (Boolean)get());
        }
        else if(get().getClass() == Double.class || get().getClass() == double.class){
            NTPublisher.publish(table, key, (Double)get());
        }else{
            System.out.println("Network Publisher: Type not supported! " + get().getClass().toString());
        }
        
    }
    
}
