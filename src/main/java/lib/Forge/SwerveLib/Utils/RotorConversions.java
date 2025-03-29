package lib.Forge.SwerveLib.Utils;

/**
 * A utility class for converting rotor values to wheel values for swerve drive calculations.
 * This includes conversions between rotations, RPM, meters, and radians.
 */
public class RotorConversions{

    private RotorConversions(){}

    /**
     * Converts rotor rotations to distance traveled in meters.
     *
     * @param rotations The number of rotor rotations.
     * @param reduction The gear reduction factor.
     * @param wheelRadiusInMeters The radius of the wheel in meters.
     * @return The distance traveled in meters.
     */
    public static double rotorRotationsToMeters(double rotations, double reduction, double WheelRadiusInMeters) {
        return rotations * (2 * Math.PI * WheelRadiusInMeters) / reduction;
    }

    /**
     * Converts rotor RPM to wheel speed in meters per second.
     *
     * @param RPM The rotor speed in rotations per minute.
     * @param reduction The gear reduction factor.
     * @param wheelRadiusInMeters The radius of the wheel in meters.
     * @return The wheel speed in meters per second.
     */
    public static double rotorRPMtoMetersPerSec(double RPM, double reduction, double WheelRadiusInMeters) {
        return (RPM * (2 * Math.PI * WheelRadiusInMeters)) / (60 * reduction);
    }

    /**
     * Converts rotor rotations to wheel angle in radians.
     *
     * @param rotations The number of rotor rotations.
     * @param reduction The gear reduction factor.
     * @return The wheel rotation in radians.
     */
    public static double rotorRotationsToWheelRadians(double rotations, double reduction) {
        return (rotations * 2 * Math.PI) / reduction;
    }

    /**
     * Converts rotor RPM to wheel angular velocity in radians per second.
     *
     * @param RPM The rotor speed in rotations per minute.
     * @param reduction The gear reduction factor.
     * @return The wheel angular velocity in radians per second.
     */
    public static double rotorRotationsToWheelRadPerSec(double RPM, double reduction) {
        return (RPM * 2 * Math.PI) / (60 * reduction);
    }

    /**
     * Converts linear velocity (meters per second) to wheel angular velocity in radians per second.
     *
     * @param mps The speed in meters per second.
     * @param wheelRadiusInMeters The radius of the wheel in meters.
     * @return The wheel angular velocity in radians per second.
     */
    public static double metersPerSecToWheelRadPerSec(double mps, double WheelRadiusInMeters) {
        return mps / WheelRadiusInMeters;
    }

    /**
     * Converts a linear distance in meters to a wheel rotation angle in radians.
     *
     * @param meters The distance traveled in meters.
     * @param wheelRadiusInMeters The radius of the wheel in meters.
     * @return The wheel rotation in radians.
     */
    public static double metersToWheelRadians(double meters, double WheelRadiusInMeters) {
        return meters / WheelRadiusInMeters;
    }

    /**
     * Converts linear velocity (meters per second) to rotor speed in RPM.
     *
     * @param mps The speed in meters per second.
     * @param reduction The gear reduction factor.
     * @param wheelRadiusInMeters The radius of the wheel in meters.
     * @return The rotor speed in RPM.
     */
    public static double metersPerSecToRotorRPM(double mps, double reduction, double WheelRadiusInMeters) {
        return (mps * 60 * reduction) / (2 * Math.PI * WheelRadiusInMeters);
    }

    /**
     * Converts an angular distance in radians to rotor rotations.
     *
     * @param radians The rotation in radians.
     * @param reduction The gear reduction factor.
     * @return The number of rotor rotations.
     */
    public static double radiansToWheelRotations(double radians, double reduction) {
        return (radians * reduction) / (2 * Math.PI);
    }

    /**
     * Converts angular velocity in radians per second to rotor speed in RPM.
     *
     * @param radPerSec The angular velocity in radians per second.
     * @param reduction The gear reduction factor.
     * @return The rotor speed in RPM.
     */
    public static double radiansPerSecToWheelRPM(double radPerSec, double reduction) {
        return (radPerSec * 60 * reduction) / (2 * Math.PI);
    }
}