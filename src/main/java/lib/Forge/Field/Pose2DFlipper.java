package lib.Forge.Field;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public class Pose2DFlipper {

    /**
     * Flips the given {@code Pose2d} around the field, inverting its Y-coordinate relative to the field's width.
     * The rotation is also modified by adjusting its angle using sine and cosine components.
     * 
     * @param pose The {@code Pose2d} to be flipped.
     * @param field The {@code FieldObject} representing the field's dimensions (specifically its width).
     * @return A new {@code Pose2d} representing the flipped pose.
     */
    public static Pose2d flip(Pose2d pose, FieldObject field){
        return new Pose2d(pose.getX(), field.width() - pose.getY(), new Rotation2d(pose.getRotation().getCos() -pose.getRotation().getSin()));
    }

}
