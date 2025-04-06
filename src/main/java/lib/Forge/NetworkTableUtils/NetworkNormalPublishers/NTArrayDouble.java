package lib.Forge.NetworkTableUtils.NetworkNormalPublishers;

import edu.wpi.first.networktables.DoubleArrayPublisher;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * A utility class for publishing arrays of double values to NetworkTables.
 */
public class NTArrayDouble {
    private DoubleArrayPublisher publisher;

    /**
     * Creates a new {@code NTArrayDouble} instance that publishes double arrays to the specified NetworkTables key.
     *
     * @param key The NetworkTables key where the double array will be published.
     */
    public NTArrayDouble(String key){
        this.publisher = NetworkTableInstance.getDefault().getDoubleArrayTopic(key).publish();
    }

    /**
     * Creates a new {@code NTArrayDouble} instance that publishes double arrays to the specified NetworkTables key.
     * @param table The NetworkTables table
     * @param key The NetworkTables key where the double array will be published.
     */
    public NTArrayDouble(String table,String key){
        this.publisher = NetworkTableInstance.getDefault().getTable(table).getDoubleArrayTopic(key).publish();
    }

    /**
     * Publishes an array of double values to NetworkTables.
     *
     * @param value The double array to send.
     */
    public void sendDouble(double[] value){
        publisher.set(value);
    }

}
