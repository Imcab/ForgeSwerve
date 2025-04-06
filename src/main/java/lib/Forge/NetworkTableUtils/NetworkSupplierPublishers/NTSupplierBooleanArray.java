package lib.Forge.NetworkTableUtils.NetworkSupplierPublishers;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;
import java.util.function.Supplier;

/**
 * A utility class for publishing boolean array values to NetworkTables using a {@link Supplier}.
 * The boolean array value is fetched from the provided supplier and published to the specified NetworkTables entry.
 */
public class NTSupplierBooleanArray{
    private final NetworkTableEntry entry;
    private final Supplier<Boolean[]> supplier;

    /**
     * Creates a new {@code NTSupplierBooleanArray} instance that publishes boolean array values to the specified
     * NetworkTables key, using the given {@link Supplier} to fetch the value.
     *
     * @param key The NetworkTables key where the boolean array value will be published.
     * @param supplier A {@link Supplier} that provides the boolean array value to be published.
     */
    public NTSupplierBooleanArray(String key, Supplier<Boolean[]> supplier) {
        this.entry = NetworkTableInstance.getDefault().getEntry(key);
        this.supplier = supplier;
    }

    /**
     * Creates a new {@code NTSupplierBooleanArray} instance that publishes boolean array values to the specified
     * NetworkTables key, using the given {@link Supplier} to fetch the value.
     * @param table
     * @param key The NetworkTables key where the boolean array value will be published.
     * @param supplier A {@link Supplier} that provides the boolean array value to be published.
     */
    public NTSupplierBooleanArray(String table, String key, Supplier<Boolean[]> supplier) {
        this.entry = NetworkTableInstance.getDefault().getTable(table).getEntry(key);
        this.supplier = supplier;
    }

    /**
     * Updates the value in NetworkTables by getting the boolean array value from the {@link Supplier}.
     * The value will be set on the specified entry in NetworkTables.
     */
    public void update() {
        entry.setBooleanArray(supplier.get());
    }
}
