package lib.Forge.NetworkTableUtils.NetworkNormalPublishers;

import edu.wpi.first.networktables.BooleanArrayPublisher;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * A utility class for publishing and retrieving boolean arrays from NetworkTables.
 */
public class NTArrayBoolean{
    private BooleanArrayPublisher publisher;

    /**
     * Creates a new {@code NTArrayBoolean} instance that publishes boolean arrays to the specified NetworkTables key.
     *
     * @param key The NetworkTables key where the boolean array will be published.
     */
    public NTArrayBoolean(String key){
        this.publisher = NetworkTableInstance.getDefault().getBooleanArrayTopic(key).publish();
    }

    /**
     * Creates a new {@code NTArrayBoolean} instance that publishes boolean arrays to the specified NetworkTables key.
     * @param table The NetworkTables table
     * @param key The NetworkTables key where the boolean array will be published.
     */
    public NTArrayBoolean(String table,String key){
        this.publisher = NetworkTableInstance.getDefault().getTable(table).getBooleanArrayTopic(key).publish();
    }

    /**
     * Publishes a boolean array to NetworkTables.
     *
     * @param value The boolean array to send.
     */
    public void sendBoolean(boolean[] value){
        publisher.set(value);
    }



}
