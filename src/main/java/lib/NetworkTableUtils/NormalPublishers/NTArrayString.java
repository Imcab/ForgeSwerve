package lib.NetworkTableUtils.NormalPublishers;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringArrayPublisher;

public class NTArrayString {

    private StringArrayPublisher publisher;

    public NTArrayString(String key){
        this.publisher = NetworkTableInstance.getDefault().getStringArrayTopic(key).publish();
    }

    public void sendString(String[] value){
        publisher.set(value);
    }
}
