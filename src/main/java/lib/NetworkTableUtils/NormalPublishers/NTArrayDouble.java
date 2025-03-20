package lib.NetworkTableUtils.NormalPublishers;

import edu.wpi.first.networktables.DoubleArrayPublisher;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NTArrayDouble {
    private DoubleArrayPublisher publisher;

    public NTArrayDouble(String key){
        this.publisher = NetworkTableInstance.getDefault().getDoubleArrayTopic(key).publish();
    }

    public void sendDouble(double[] value){
        publisher.set(value);
    }

}
