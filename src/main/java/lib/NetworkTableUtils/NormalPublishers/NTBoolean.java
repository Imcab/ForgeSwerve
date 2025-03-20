package lib.NetworkTableUtils.NormalPublishers;

import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NTBoolean {
    private BooleanPublisher publisher;

    public NTBoolean(String key){
        this.publisher = NetworkTableInstance.getDefault().getBooleanTopic(key).publish();
    }

    public void sendBoolean(boolean value){
        publisher.set(value);
    }
    
}
