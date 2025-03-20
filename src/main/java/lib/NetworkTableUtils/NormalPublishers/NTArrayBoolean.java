package lib.NetworkTableUtils.NormalPublishers;

import edu.wpi.first.networktables.BooleanArrayPublisher;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NTArrayBoolean{
    private BooleanArrayPublisher publisher;

    public NTArrayBoolean(String key){
        this.publisher = NetworkTableInstance.getDefault().getBooleanArrayTopic(key).publish();
    }

    public void sendBoolean(boolean[] value){
        publisher.set(value);
    }

}
