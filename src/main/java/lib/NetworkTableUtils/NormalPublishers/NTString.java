package lib.NetworkTableUtils.NormalPublishers;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringPublisher;

public class NTString{
    private StringPublisher publisher;

    public NTString(String key){
        this.publisher = NetworkTableInstance.getDefault().getStringTopic(key).publish();
    }

    public void sendString(String value){
        publisher.set(value);
    }
    
}
