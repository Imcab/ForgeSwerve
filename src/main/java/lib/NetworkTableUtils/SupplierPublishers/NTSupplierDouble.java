package lib.NetworkTableUtils.SupplierPublishers;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import java.util.function.Supplier;

public class NTSupplierDouble{

    private final DoublePublisher publisher;
    private final Supplier<Double> supplier;

    public NTSupplierDouble(String key, Supplier<Double> supplier) {
        this.publisher = NetworkTableInstance.getDefault().getDoubleTopic(key).publish();
        this.supplier = supplier;
    }

    public void update() {
        publisher.set(supplier.get());
    }
}
