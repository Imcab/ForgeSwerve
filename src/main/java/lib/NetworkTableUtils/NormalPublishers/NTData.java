package lib.NetworkTableUtils.NormalPublishers;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.util.struct.Struct;

public class NTData<DataType>{

    private StructPublisher<DataType> publisher;
    
    public NTData(String key, Struct<DataType> struct) {
        this.publisher = NetworkTableInstance.getDefault().getStructTopic(key, struct).publish();
    }

    public void sendData(DataType value){
        publisher.set(value);
    }

}
