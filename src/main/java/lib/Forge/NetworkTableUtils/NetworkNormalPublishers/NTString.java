package lib.Forge.NetworkTableUtils.NetworkNormalPublishers;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringPublisher;

/**
 * A utility class for publishing string values to NetworkTables.
 */
public class NTString{
    private StringPublisher publisher;

    /**
     * Creates a new {@code NTString} instance that publishes string values to the specified NetworkTables key.
     *
     * @param key The NetworkTables key where the string value will be published.
     */
    public NTString(String key){
        this.publisher = NetworkTableInstance.getDefault().getStringTopic(key).publish();
    }

    /**
     * Creates a new {@code NTString} instance that publishes string values to the specified NetworkTables key.
     * @param table The NetworkTables table
     * @param key The NetworkTables key where the string value will be published.
     */
    public NTString(String table, String key){
        this.publisher = NetworkTableInstance.getDefault().getTable(table).getStringTopic(key).publish();
    }

    /**
     * Publishes a string value to NetworkTables.
     *
     * @param value The string value to send.
     */
    public void sendString(String value){
        publisher.set(value);
    }
    
}
