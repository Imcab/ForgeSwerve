package lib.NetworkTableUtils.NormalPublishers;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NTDouble{

    private DoublePublisher publisher;

    public NTDouble(String key){
        this.publisher = NetworkTableInstance.getDefault().getDoubleTopic(key).publish();
    }

    public void sendDouble(Double value){
        publisher.set(value);
    }

}
