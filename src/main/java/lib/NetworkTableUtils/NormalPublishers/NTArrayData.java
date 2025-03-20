package lib.NetworkTableUtils.NormalPublishers;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.util.struct.Struct;

public class NTArrayData<DataType>{

    private StructArrayPublisher<DataType> publisher;
    
    public NTArrayData(String key, Struct<DataType> struct) {
        this.publisher = NetworkTableInstance.getDefault().getStructArrayTopic(key, struct).publish();
    }

    public void sendData(DataType[] value){
        publisher.set(value);
    }
}
