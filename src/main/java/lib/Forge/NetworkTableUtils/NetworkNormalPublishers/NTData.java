package lib.Forge.NetworkTableUtils.NetworkNormalPublishers;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.util.struct.Struct;

/**
 * A generic utility class for publishing a single structured data object to NetworkTables.
 *
 * @param <DataType> The type of structured data being published.
 */
public class NTData<DataType>{

    private StructPublisher<DataType> publisher;
    
    /**
     * Creates a new {@code NTData} instance that publishes a structured data object to NetworkTables.
     *
     * @param key    The NetworkTables key where the data will be published.
     * @param struct The {@link Struct} definition for the data type.
     */
    public NTData(String key, Struct<DataType> struct) {
        this.publisher = NetworkTableInstance.getDefault().getStructTopic(key, struct).publish();
    }

    /**
     * Creates a new {@code NTData} instance that publishes a structured data object to NetworkTables.
     * @param table The NetworkTables table
     * @param key    The NetworkTables key where the data will be published.
     * @param struct The {@link Struct} definition for the data type.
     */
    public NTData(String table, String key, Struct<DataType> struct) {
        this.publisher = NetworkTableInstance.getDefault().getTable(table).getStructTopic(key, struct).publish();
    }

    /**
     * Publishes a structured data object to NetworkTables.
     *
     * @param value The data object to send.
     */
    public void sendData(DataType value){
        publisher.set(value);
    }

}
