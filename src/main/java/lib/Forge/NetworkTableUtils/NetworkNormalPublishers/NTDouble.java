package lib.Forge.NetworkTableUtils.NetworkNormalPublishers;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * A utility class for publishing double values to NetworkTables.
 */
public class NTDouble{

    private DoublePublisher publisher;

    /**
     * Creates a new {@code NTDouble} instance that publishes double values to the specified NetworkTables key.
     *
     * @param key The NetworkTables key where the double value will be published.
     */
    public NTDouble(String key){
        this.publisher = NetworkTableInstance.getDefault().getDoubleTopic(key).publish();
    }

    /**
     * Creates a new {@code NTDouble} instance that publishes double values to the specified NetworkTables key.
     * @param table The NetworkTables table
     * @param key The NetworkTables key where the double value will be published.
     */
    public NTDouble(String table, String key){
        this.publisher = NetworkTableInstance.getDefault().getTable(table).getDoubleTopic(key).publish();
    }

    /**
     * Publishes a double value to NetworkTables.
     *
     * @param value The double value to send.
     */
    public void sendDouble(Double value){
        publisher.set(value);
    }

}
