package lib.NetworkTableUtils.MultipleData;

import java.util.HashMap;
import java.util.Map;

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
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * Utility class for publishing and retrieving various data types from NetworkTables.
 * This class allows for easy interaction with the NetworkTables API using structured and primitive data types.
 */
public class NTPublisher{

    private static final NetworkTableInstance ntInstance = NetworkTableInstance.getDefault();
   
    private static final Map<String, Sendable> sendables = new HashMap<>();

    private NTPublisher(){
        throw new UnsupportedOperationException("This is an Utility class");
    }

    /**
     * Publishes a double value to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The double value to publish.
     */
    public static void publish(String tableName, String key, double value){
        ntInstance.getTable(tableName).getEntry(key).setDouble(value);
    }

    /**
     * Publishes a boolean value to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The boolean value to publish.
     */
    public static void publish(String tableName, String key, boolean value){
        ntInstance.getTable(tableName).getEntry(key).setBoolean(value);
    }

    /**
     * Publishes a String value to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The String value to publish.
     */
    public static void publish(String tableName, String key, String value){
        ntInstance.getTable(tableName).getEntry(key).setString(value);
    }

    /**
     * Publishes an array of doubles to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The double array to publish.
     */
    public static void publish(String tableName, String key, double[] value){
        ntInstance.getTable(tableName).getEntry(key).setDoubleArray(value);
    }

    /**
     * Publishes an array of booleans to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The boolean array to publish.
     */
    public static void publish(String tableName, String key, boolean[] value){
        ntInstance.getTable(tableName).getEntry(key).setBooleanArray(value);
    }

    /**
     * Publishes an array of Strings to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The String array to publish.
     */
    public static void publish(String tableName, String key, String[] value){
        ntInstance.getTable(tableName).getEntry(key).setStringArray(value);
    }

    /**
     * Publishes a {@link ChassisSpeeds} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link ChassisSpeeds} object to publish.
     */
    public static void publish(String tableName, String key, ChassisSpeeds value){
        ntInstance.getTable(tableName).getStructTopic(key, ChassisSpeeds.struct).publish().set(value);
    }

    /**
     * Publishes an array of {@link ChassisSpeeds} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link ChassisSpeeds} to publish.
     */
    public static void publish(String tableName, String key, ChassisSpeeds[] value){
        ntInstance.getTable(tableName).getStructArrayTopic(key, ChassisSpeeds.struct).publish().set(value);
    }

    /**
     * Publishes a {@link Pose2d} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Pose2d} object to publish.
     */
    public static void publish(String tableName, String key, Pose2d value){
        ntInstance.getTable(tableName).getStructTopic(key, Pose2d.struct).publish().set(value);
    }

    /**
     * Publishes an array of {@link Pose2d} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Pose2d} to publish.
     */
    public static void publish(String tableName, String key, Pose2d[] value){
        ntInstance.getTable(tableName).getStructArrayTopic(key, Pose2d.struct).publish().set(value);
    }


    /**
     * Publishes a {@link Pose3d} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Pose3d} object to publish.
     */
    public static void publish(String tableName, String key, Pose3d value){
        ntInstance.getTable(tableName).getStructTopic(key, Pose3d.struct).publish().set(value);
    }

    /**
     * Publishes an array of {@link Pose3d} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Pose3d} to publish.
     */
    public static void publish(String tableName, String key, Pose3d[] value){
        ntInstance.getTable(tableName).getStructArrayTopic(key, Pose3d.struct).publish().set(value);
    }

    /**
     * Publishes a {@link Rotation2d} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Rotation2d} object to publish.
     */
    public static void publish(String tableName, String key, Rotation2d value){
        ntInstance.getTable(tableName).getStructTopic(key, Rotation2d.struct).publish().set(value);
    }

    /**
     * Publishes an array of {@link Rotation2d} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Rotation2d} to publish.
     */
    public static void publish(String tableName, String key, Rotation2d[] value){
        ntInstance.getTable(tableName).getStructArrayTopic(key, Rotation2d.struct).publish().set(value);
    }

    /**
     * Publishes a {@link Rotation3d} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Rotation3d} object to publish.
     */
    public static void publish(String tableName, String key, Rotation3d value){
        ntInstance.getTable(tableName).getStructTopic(key, Rotation3d.struct).publish().set(value);
    }

    /**
     * Publishes an array of {@link Rotation3d} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Rotation3d} to publish.
     */
    public static void publish(String tableName, String key, Rotation3d[] value){
        ntInstance.getTable(tableName).getStructArrayTopic(key, Rotation3d.struct).publish().set(value);
    }

    /**
     * Publishes a {@link Transform2d} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Transform2d} object to publish.
     */
    public static void publish(String tableName, String key, Transform2d value){
        ntInstance.getTable(tableName).getStructTopic(key, Transform2d.struct).publish().set(value);
    }

    /**
     * Publishes an array of {@link Transform2d} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Transform2d} to publish.
     */
    public static void publish(String tableName, String key, Transform2d[] value){
        ntInstance.getTable(tableName).getStructArrayTopic(key, Transform2d.struct).publish().set(value);
    }

    /**
     * Publishes a {@link Transform3d} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Transform3d} object to publish.
     */
    public static void publish(String tableName, String key, Transform3d value){
        ntInstance.getTable(tableName).getStructTopic(key, Transform3d.struct).publish().set(value);
    }

    /**
     * Publishes an array of {@link Transform3d} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Transform3d} to publish.
     */
    public static void publish(String tableName, String key, Transform3d[] value){
        ntInstance.getTable(tableName).getStructArrayTopic(key, Transform3d.struct).publish().set(value);
    }

    /**
     * Publishes a {@link Translation2d} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Translation2d} object to publish.
     */
    public static void publish(String tableName, String key, Translation2d value){
        ntInstance.getTable(tableName).getStructTopic(key, Translation2d.struct).publish().set(value);
    }

    /**
     * Publishes an array of {@link Translation2d} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Translation2d} to publish.
     */
    public static void publish(String tableName, String key, Translation2d[] value){
        ntInstance.getTable(tableName).getStructArrayTopic(key, Translation2d.struct).publish().set(value);
    }

    /**
     * Publishes a {@link Translation3d} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Translation3d} object to publish.
     */
    public static void publish(String tableName, String key, Translation3d value){
        ntInstance.getTable(tableName).getStructTopic(key, Translation3d.struct).publish().set(value);
    }

    /**
     * Publishes an array of {@link Translation3d} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Translation3d} to publish.
     */
    public static void publish(String tableName, String key, Translation3d[] value){
        ntInstance.getTable(tableName).getStructArrayTopic(key, Translation3d.struct).publish().set(value);
    }

    /**
     * Publishes a {@link SwerveModuleState} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link SwerveModuleState} object to publish.
     */
    public static void publish(String tableName, String key, SwerveModuleState value){
        ntInstance.getTable(tableName).getStructTopic(key, SwerveModuleState.struct).publish().set(value);
    }

    /**
     * Publishes an array of {@link SwerveModuleState} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link SwerveModuleState} to publish.
     */
    public static void publish(String tableName, String key, SwerveModuleState[] value){
        ntInstance.getTable(tableName).getStructArrayTopic(key, SwerveModuleState.struct).publish().set(value);
    }

    /**
     * Publishes a {@link SwerveModulePosition} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link SwerveModulePosition} object to publish.
     */
    public static void publish(String tableName, String key, SwerveModulePosition value){
        ntInstance.getTable(tableName).getStructTopic(key, SwerveModulePosition.struct).publish().set(value);
    }

    /**
     * Publishes an array of {@link SwerveModulePosition} object to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link SwerveModulePosition} to publish.
     */
    public static void publish(String tableName, String key, SwerveModulePosition[] value){
        ntInstance.getTable(tableName).getStructArrayTopic(key, SwerveModulePosition.struct).publish().set(value);
    }

    /**
     * Publishes a {@link Color} value as a hex string to NetworkTables.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Color} object to publish.
     */
    public static void publish(String tableName, String key, Sendable value) {
        String fullPath = String.format("%s/%s", tableName, key);
        if (!sendables.containsKey(fullPath) || sendables.get(fullPath) != value) {
            sendables.put(fullPath, value);
            NetworkTable dataTable = NetworkTableInstance.getDefault().getTable(fullPath);
            SendableBuilderImpl builder = new SendableBuilderImpl();
            builder.setTable(dataTable);
            SendableRegistry.publish(value, builder);
            builder.startListeners();
            dataTable.getEntry(".name").setString(fullPath);
        }
    }

    /**
     * Publishes a {@link Sendable} object to NetworkTables.
     * Ensures that the sendable is only added once per unique table entry.
     *
     * @param tableName The name of the table.
     * @param key       The key for the entry.
     * @param value     The {@link Sendable} object to publish.
     */
    public static void publish(String tableName, String key, Color value){
        ntInstance.getTable(tableName).getEntry(key).setString(value.toHexString());
    }

    /**
     * Updates all registered {@link Sendable} objects in NetworkTables. <b>Must call this method periodically</b>
     */
    public static void updateAllSendables() {
        for (Sendable sendable : sendables.values()) {
            SendableRegistry.update(sendable);
        }
    }

    /**
     * Retrieves a double value from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default value if the key is not found.
     * @return The retrieved double value.
     */
    public static double retrieve(String TableName, String key, double defaultValue){
        return ntInstance.getTable(TableName).getEntry(key).getDouble(defaultValue);
    }

    /**
     * Retrieves a boolean value from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default value if the key is not found.
     * @return The retrieved boolean value.
     */
    public static boolean retrieve(String TableName, String key, boolean defaultValue){
        return ntInstance.getTable(TableName).getEntry(key).getBoolean(defaultValue);
    }

    /**
     * Retrieves an array of double values from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default array if the key is not found.
     * @return The retrieved array of double values.
     */
    public static double[] retrieve(String TableName, String key, double[] defaultValue){
        return ntInstance.getTable(TableName).getEntry(key).getDoubleArray(defaultValue);
    }

    /**
     * Retrieves an array of boolean values from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default array if the key is not found.
     * @return The retrieved array of boolean values.
     */
    public static boolean[] retrieve(String TableName, String key, boolean[] defaultValue){
        return ntInstance.getTable(TableName).getEntry(key).getBooleanArray(defaultValue);
    }

    /**
     * Retrieves a String value from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default value if the key is not found.
     * @return The retrieved String value.
     */
    public static String retrieve(String TableName, String key, String defaultValue){
        return ntInstance.getTable(TableName).getEntry(key).getString(defaultValue);
    }

    /**
     * Retrieves an array of String values from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default array if the key is not found.
     * @return The retrieved array of String values.
     */
    public static String[] retrieve(String TableName, String key, String[] defaultValue){
        return ntInstance.getTable(TableName).getEntry(key).getStringArray(defaultValue);
    }

    /**
     * Retrieves a {@link Pose2d} object from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default value if the key is not found.
     * @return The retrieved {@link Pose2d} object.
     */
    public static Pose2d retrieve(String TableName, String key, Pose2d defaultValue){
        return ntInstance.getTable(TableName).getStructTopic(key, Pose2d.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves an array of {@link Pose2d} objects from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default array if the key is not found.
     * @return The retrieved array of {@link Pose2d} objects.
     */
    public static Pose2d[] retrieve(String TableName, String key, Pose2d[] defaultValue){
        return ntInstance.getTable(TableName).getStructArrayTopic(key, Pose2d.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves a {@link Pose3d} object from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default value if the key is not found.
     * @return The retrieved {@link Pose3d} object.
     */
    public static Pose3d retrieve(String TableName, String key, Pose3d defaultValue){
        return ntInstance.getTable(TableName).getStructTopic(key, Pose3d.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves an array of {@link Pose3d} objects from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default array if the key is not found.
     * @return The retrieved array of {@link Pose3d} objects.
     */
    public static Pose3d[] retrieve(String TableName, String key, Pose3d[] defaultValue){
        return ntInstance.getTable(TableName).getStructArrayTopic(key, Pose3d.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves a {@link Translation2d} object from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default value if the key is not found.
     * @return The retrieved {@link Translation2d} object.
     */
    public static Translation2d retrieve(String TableName, String key, Translation2d defaultValue){
        return ntInstance.getTable(TableName).getStructTopic(key, Translation2d.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves an array of {@link Translation2d} objects from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default array if the key is not found.
     * @return The retrieved array of {@link Translation2d} objects.
     */
    public static Translation2d[] retrieve(String TableName, String key, Translation2d[] defaultValue){
        return ntInstance.getTable(TableName).getStructArrayTopic(key, Translation2d.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves a {@link Translation3d} object from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default value if the key is not found.
     * @return The retrieved {@link Translation3d} object.
     */
    public static Translation3d retrieve(String TableName, String key, Translation3d defaultValue){
        return ntInstance.getTable(TableName).getStructTopic(key, Translation3d.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves an array of {@link Translation3d} objects from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default array if the key is not found.
     * @return The retrieved array of {@link Translation3d} objects.
     */
    public static Translation3d[] retrieve(String TableName, String key, Translation3d[] defaultValue){
        return ntInstance.getTable(TableName).getStructArrayTopic(key, Translation3d.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves a {@link Transform2d} object from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default value if the key is not found.
     * @return The retrieved {@link Transform2d} object.
     */
    public static Transform2d retrieve(String TableName, String key, Transform2d defaultValue){
        return ntInstance.getTable(TableName).getStructTopic(key, Transform2d.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves an array of {@link Transform2d} objects from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default array if the key is not found.
     * @return The retrieved array of {@link Transform2d} objects.
     */
    public static Transform2d[] retrieve(String TableName, String key, Transform2d[] defaultValue){
        return ntInstance.getTable(TableName).getStructArrayTopic(key, Transform2d.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves a {@link Transform3d} object from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default value if the key is not found.
     * @return The retrieved {@link Transform3d} object.
     */
    public static Transform3d retrieve(String TableName, String key, Transform3d defaultValue){
        return ntInstance.getTable(TableName).getStructTopic(key, Transform3d.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves an array of {@link Transform3d} objects from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default array if the key is not found.
     * @return The retrieved array of {@link Transform3d} objects.
     */
    public static Transform3d[] retrieve(String TableName, String key, Transform3d[] defaultValue){
        return ntInstance.getTable(TableName).getStructArrayTopic(key, Transform3d.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves a {@link Rotation2d} object from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default value if the key is not found.
     * @return The retrieved {@link Rotation2d} object.
     */
    public static Rotation2d retrieve(String TableName, String key, Rotation2d defaultValue){
        return ntInstance.getTable(TableName).getStructTopic(key, Rotation2d.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves an array of {@link Rotation2d} objects from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default array if the key is not found.
     * @return The retrieved array of {@link Rotation2d} objects.
     */
    public static Rotation2d[] retrieve(String TableName, String key, Rotation2d[] defaultValue){
        return ntInstance.getTable(TableName).getStructArrayTopic(key, Rotation2d.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves a {@link Rotation3d} object from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default value if the key is not found.
     * @return The retrieved {@link Rotation3d} object.
     */
    public static Rotation3d retrieve(String TableName, String key, Rotation3d defaultValue){
        return ntInstance.getTable(TableName).getStructTopic(key, Rotation3d.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves an array of {@link Rotation3d} objects from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default array if the key is not found.
     * @return The retrieved array of {@link Rotation3d} objects.
     */
    public static Rotation3d[] retrieve(String TableName, String key, Rotation3d[] defaultValue){
        return ntInstance.getTable(TableName).getStructArrayTopic(key, Rotation3d.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves a {@link ChassisSpeeds} object from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default value if the key is not found.
     * @return The retrieved {@link ChassisSpeeds} object.
     */
    public static ChassisSpeeds retrieve(String TableName, String key, ChassisSpeeds defaultValue){
        return ntInstance.getTable(TableName).getStructTopic(key, ChassisSpeeds.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves an array of {@link ChassisSpeeds} objects from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default array if the key is not found.
     * @return The retrieved array of {@link ChassisSpeeds} objects.
     */
    public static ChassisSpeeds[] retrieve(String TableName, String key, ChassisSpeeds[] defaultValue){
        return ntInstance.getTable(TableName).getStructArrayTopic(key, ChassisSpeeds.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves a {@link SwerveModuleState} object from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default value if the key is not found.
     * @return The retrieved {@link SwerveModuleState} object.
     */
    public static SwerveModuleState retrieve(String TableName, String key, SwerveModuleState defaultValue){
        return ntInstance.getTable(TableName).getStructTopic(key, SwerveModuleState.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves an array of {@link SwerveModuleState} objects from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default array if the key is not found.
     * @return The retrieved array of {@link SwerveModuleState} objects.
     */
    public static SwerveModuleState[] retrieve(String TableName, String key, SwerveModuleState[] defaultValue){
        return ntInstance.getTable(TableName).getStructArrayTopic(key, SwerveModuleState.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves a {@link SwerveModulePosition} object from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default value if the key is not found.
     * @return The retrieved {@link SwerveModulePosition} object.
     */
    public static SwerveModulePosition retrieve(String TableName, String key, SwerveModulePosition defaultValue){
        return ntInstance.getTable(TableName).getStructTopic(key, SwerveModulePosition.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves an array of {@link SwerveModulePosition} objects from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default array if the key is not found.
     * @return The retrieved array of {@link SwerveModulePosition} objects.
     */
    public static SwerveModulePosition[] retrieve(String TableName, String key, SwerveModulePosition[] defaultValue){
        return ntInstance.getTable(TableName).getStructArrayTopic(key, SwerveModulePosition.struct).getEntry(defaultValue).get();
    }

    /**
     * Retrieves a {@link Color} value from NetworkTables.
     *
     * @param tableName    The name of the table.
     * @param key          The key for the entry.
     * @param defaultValue The default {@link Color} if the key is not found.
     * @return The retrieved {@link Color} object.
     */
    public static Color retrieve(String TableName, String key, Color defaultValue){
        return new Color(ntInstance.getTable(TableName).getEntry(key).getString(defaultValue.toHexString()));
    } 

}
