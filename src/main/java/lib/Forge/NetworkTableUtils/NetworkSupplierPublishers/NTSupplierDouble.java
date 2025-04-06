package lib.Forge.NetworkTableUtils.NetworkSupplierPublishers;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import java.util.function.Supplier;

/**
 * A utility class for publishing double values to NetworkTables using a {@link Supplier}.
 * The double value is fetched from the provided supplier and published to the specified NetworkTables entry.
 */
public class NTSupplierDouble{

    private final DoublePublisher publisher;
    private final Supplier<Double> supplier;

    /**
     * Creates a new {@code NTSupplierDouble} instance that publishes double values to the specified
     * NetworkTables key, using the given {@link Supplier} to fetch the value.
     *
     * @param key The NetworkTables key where the double value will be published.
     * @param supplier A {@link Supplier} that provides the double value to be published.
     */
    public NTSupplierDouble(String key, Supplier<Double> supplier) {
        this.publisher = NetworkTableInstance.getDefault().getDoubleTopic(key).publish();
        this.supplier = supplier;
    }

    /**
     * Creates a new {@code NTSupplierDouble} instance that publishes double values to the specified
     * NetworkTables key, using the given {@link Supplier} to fetch the value.
     * @param table
     * @param key The NetworkTables key where the double value will be published.
     * @param supplier A {@link Supplier} that provides the double value to be published.
     */
    public NTSupplierDouble(String table, String key, Supplier<Double> supplier) {
        this.publisher = NetworkTableInstance.getDefault().getTable(table).getDoubleTopic(key).publish();
        this.supplier = supplier;
    }

    /**
     * Updates the value in NetworkTables by getting the double value from the {@link Supplier}.
     * The value will be set on the specified entry in NetworkTables.
     */
    public void update() {
        publisher.set(supplier.get());
    }
}
