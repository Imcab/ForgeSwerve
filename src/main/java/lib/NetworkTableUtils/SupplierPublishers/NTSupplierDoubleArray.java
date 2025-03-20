package lib.NetworkTableUtils.SupplierPublishers;

import java.util.function.Supplier;

import edu.wpi.first.networktables.DoubleArrayPublisher;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NTSupplierDoubleArray {

    private final DoubleArrayPublisher publisher;
    private final Supplier<double[]> supplier;

    public NTSupplierDoubleArray(String key, Supplier<double[]> supplier) {
        this.publisher = NetworkTableInstance.getDefault().getDoubleArrayTopic(key).publish();
        this.supplier = supplier;
    }

    public void update() {
        publisher.set(supplier.get());
    }
}
