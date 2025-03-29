package lib.Forge.Equals;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Translation2d;

/**
 * The {@code ThreeDimensionalSpace} class defines a 3D space using X, Y, and Z coordinates,
 * along with a tolerance that represents the allowable deviation in all three axes.
 * This class provides methods to check if a given point (or pose) is within this defined 3D space.
 */
public class ThreeDimensionalSpace {

    private final Domain xDomain;
    private final Domain yDomain;
    private final Domain zDomain;
    private final double tolerance;
    private final Translation3d translation;

    /**
     * Creates a 3D space with specified X, Y, and Z coordinates and a tolerance value.
     * 
     * @param x The X-coordinate of the space.
     * @param y The Y-coordinate of the space.
     * @param z The Z-coordinate of the space.
     * @param tolerance The allowable deviation for each axis (percentage of the coordinates).
     */
    public ThreeDimensionalSpace(double x, double y, double z, double tolerance) {
        this(new Translation3d(x, y, z), tolerance);
    }

    /**
     * Creates a 3D space based on a given {@code Translation3d} object and a tolerance value.
     * 
     * @param xyz The 3D coordinate (X, Y, Z) as a {@code Translation3d} object.
     * @param tolerance The allowable deviation for each axis (percentage of the coordinates).
     */
    public ThreeDimensionalSpace(Translation3d xyz, double tolerance) {
        this.tolerance = tolerance;
        this.translation = xyz;

        double percentageInX = Math.abs(xyz.getX() * tolerance);
        double percentageInY = Math.abs(xyz.getY() * tolerance);
        double percentageInZ = Math.abs(xyz.getZ() * tolerance);

        this.xDomain = new Domain(xyz.getX() - percentageInX, xyz.getX() + percentageInX);
        this.yDomain = new Domain(xyz.getY() - percentageInY, xyz.getY() + percentageInY);
        this.zDomain = new Domain(xyz.getZ() - percentageInZ, xyz.getZ() + percentageInZ);
    }

    /**
     * Returns the {@code Translation3d} representing the center of the 3D space.
     * 
     * @return The center of the 3D space as a {@code Translation3d}.
     */
    public Translation3d asTranslation() {
        return translation;
    }

    /**
     * Returns the tolerance value used for this 3D space.
     * 
     * @return The tolerance value.
     */
    public double getTolerance() {
        return tolerance;
    }

    /**
     * Returns the range of X coordinates (min and max) for this 3D space as a {@code Translation2d}.
     * 
     * @return The range of X coordinates.
     */
    public Translation2d getXRange() {
        return new Translation2d(xDomain.minValue(), xDomain.maxValue());
    }

    /**
     * Returns the range of Y coordinates (min and max) for this 3D space as a {@code Translation2d}.
     * 
     * @return The range of Y coordinates.
     */
    public Translation2d getYRange() {
        return new Translation2d(yDomain.minValue(), yDomain.maxValue());
    }

    /**
     * Returns the range of Z coordinates (min and max) for this 3D space as a {@code Translation2d}.
     * 
     * @return The range of Z coordinates.
     */
    public Translation2d getZRange() {
        return new Translation2d(zDomain.minValue(), zDomain.maxValue());
    }

    /**
     * Checks if a given position is inside the defined 3D space.
     * @param pose The Pose3d to check.
     * @return True if the position is inside the space, false otherwise.
     */
    public boolean atSpace(Pose3d pose) {
        return xDomain.inRange(pose.getX()) &&
               yDomain.inRange(pose.getY()) &&
               zDomain.inRange(pose.getZ());
    }
}
