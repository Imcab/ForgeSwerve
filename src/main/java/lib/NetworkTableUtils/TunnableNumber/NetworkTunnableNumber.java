package lib.NetworkTableUtils.TunnableNumber;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NetworkTunnableNumber {

    private final DoubleSubscriber subscriber;
    private final DoublePublisher publisher;
    private final NetworkTableEntry entry;
    private double lastValue;

    public NetworkTunnableNumber(String key, double defaultValue){
        var instance = NetworkTableInstance.getDefault();
        this.entry = instance.getEntry(key);
        this.entry.setPersistent();
        
        var topic = instance.getDoubleTopic(key);
        this.subscriber = topic.subscribe(entry.exists() ? entry.getDouble(defaultValue) : defaultValue);
        this.publisher = topic.publish();
        this.publisher.set(entry.exists() ? entry.getDouble(defaultValue) : defaultValue);
        this.lastValue = entry.exists() ? entry.getDouble(defaultValue) : defaultValue;
    }

    public void update() {
        lastValue = subscriber.get();
    }

    public double get() {
        return lastValue;
    }

    public void set(double value) {
        if (value != lastValue) {
            lastValue = value;
            publisher.set(value);
            entry.setDouble(value);
        }
    }

    public boolean hasChanged() {
        return subscriber.get() != lastValue;
    }
}
