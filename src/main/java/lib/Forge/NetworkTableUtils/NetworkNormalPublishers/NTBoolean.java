package lib.Forge.NetworkTableUtils.NetworkNormalPublishers;

import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * A utility class for publishing boolean values to NetworkTables.
 */
public class NTBoolean {
    private BooleanPublisher publisher;

    /**
     * Creates a new {@code NTBoolean} instance that publishes boolean values to the specified NetworkTables key.
     *
     * @param key The NetworkTables key where the boolean value will be published.
     */
    public NTBoolean(String key){
        this.publisher = NetworkTableInstance.getDefault().getBooleanTopic(key).publish();
    }

    /**
     * Creates a new {@code NTBoolean} instance that publishes boolean values to the specified NetworkTables key.
     * @param table The NetworkTables table
     * @param key The NetworkTables key where the boolean value will be published.
     */
    public NTBoolean(String table, String key){
        this.publisher = NetworkTableInstance.getDefault().getTable(table).getBooleanTopic(key).publish();
    }

    /**
     * Publishes a boolean value to NetworkTables.
     *
     * @param value The boolean value to send.
     */
    public void sendBoolean(boolean value){
        publisher.set(value);
    }
    
}
