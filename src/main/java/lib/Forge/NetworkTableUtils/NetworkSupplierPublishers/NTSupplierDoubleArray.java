package lib.Forge.NetworkTableUtils.NetworkSupplierPublishers;

import java.util.function.Supplier;

import edu.wpi.first.networktables.DoubleArrayPublisher;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * A utility class for publishing double array values to NetworkTables using a {@link Supplier}.
 * The double array value is fetched from the provided supplier and published to the specified NetworkTables entry.
 */
public class NTSupplierDoubleArray {

    private final DoubleArrayPublisher publisher;
    private final Supplier<double[]> supplier;

    /**
     * Creates a new {@code NTSupplierDoubleArray} instance that publishes double array values to the specified
     * NetworkTables key, using the given {@link Supplier} to fetch the value.
     *
     * @param key The NetworkTables key where the double array value will be published.
     * @param supplier A {@link Supplier} that provides the double array value to be published.
     */
    public NTSupplierDoubleArray(String key, Supplier<double[]> supplier) {
        this.publisher = NetworkTableInstance.getDefault().getDoubleArrayTopic(key).publish();
        this.supplier = supplier;
    }

    /**
     * Creates a new {@code NTSupplierDoubleArray} instance that publishes double array values to the specified
     * NetworkTables key, using the given {@link Supplier} to fetch the value.
     * @param table
     * @param key The NetworkTables key where the double array value will be published.
     * @param supplier A {@link Supplier} that provides the double array value to be published.
     */
    public NTSupplierDoubleArray(String table, String key, Supplier<double[]> supplier) {
        this.publisher = NetworkTableInstance.getDefault().getTable(table).getDoubleArrayTopic(key).publish();
        this.supplier = supplier;
    }

    /**
     * Updates the value in NetworkTables by getting the double array value from the {@link Supplier}.
     * The value will be set on the specified entry in NetworkTables.
     */
    public void update() {
        publisher.set(supplier.get());
    }
}
