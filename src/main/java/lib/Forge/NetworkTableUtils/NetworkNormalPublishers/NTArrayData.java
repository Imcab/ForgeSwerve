package lib.Forge.NetworkTableUtils.NetworkNormalPublishers;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.util.struct.Struct;

/**
 * A generic utility class for publishing arrays of structured data to NetworkTables.
 *
 * @param <DataType> The type of structured data being published.
 */
public class NTArrayData<DataType>{

    private StructArrayPublisher<DataType> publisher;
    
    /**
     * Creates a new {@code NTArrayData} instance that publishes an array of structured data to NetworkTables.
     *
     * @param key    The NetworkTables key where the data array will be published.
     * @param struct The {@link Struct} definition for the data type.
     */
    public NTArrayData(String key, Struct<DataType> struct) {
        this.publisher = NetworkTableInstance.getDefault().getStructArrayTopic(key, struct).publish();
    }

    /**
     * Creates a new {@code NTArrayData} instance that publishes an array of structured data to NetworkTables.
     * @param table The NetworkTables table
     * @param key    The NetworkTables key where the data array will be published.
     * @param struct The {@link Struct} definition for the data type.
     */
    public NTArrayData(String table,String key, Struct<DataType> struct) {
        this.publisher = NetworkTableInstance.getDefault().getTable(table).getStructArrayTopic(key, struct).publish();
    }

    /**
     * Publishes an array of structured data to NetworkTables.
     *
     * @param value The array of data to send.
     */
    public void sendData(DataType[] value){
        publisher.set(value);
    }
}
