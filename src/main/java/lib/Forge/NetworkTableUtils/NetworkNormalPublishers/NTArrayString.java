package lib.Forge.NetworkTableUtils.NetworkNormalPublishers;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringArrayPublisher;

/**
 * A utility class for publishing arrays of strings to NetworkTables.
 */
public class NTArrayString {

    private StringArrayPublisher publisher;

    /**
     * Creates a new {@code NTArrayString} instance that publishes string arrays to the specified NetworkTables key.
     *
     * @param key The NetworkTables key where the string array will be published.
     */
    public NTArrayString(String key){
        this.publisher = NetworkTableInstance.getDefault().getStringArrayTopic(key).publish();
    }

    /**
     * Creates a new {@code NTArrayString} instance that publishes string arrays to the specified NetworkTables key.
     * @param table The NetworkTables table
     * @param key The NetworkTables key where the string array will be published.
     */
    public NTArrayString(String table, String key){
        this.publisher = NetworkTableInstance.getDefault().getTable(table).getStringArrayTopic(key).publish();
    }

    /**
     * Publishes an array of string values to NetworkTables.
     *
     * @param value The string array to send.
     */
    public void sendString(String[] value){
        publisher.set(value);
    }
}
