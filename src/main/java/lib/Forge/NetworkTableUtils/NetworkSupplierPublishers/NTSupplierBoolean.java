package lib.Forge.NetworkTableUtils.NetworkSupplierPublishers;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;
import java.util.function.Supplier;

/**
 * A utility class for publishing boolean values to NetworkTables using a {@link Supplier}.
 * The boolean value is fetched from the provided supplier and published to the specified NetworkTables entry.
 */
public class NTSupplierBoolean {
    private final NetworkTableEntry entry;
    private final Supplier<Boolean> supplier;

    /**
     * Creates a new {@code NTSupplierBoolean} instance that publishes boolean values to the specified 
     * NetworkTables key, using the given {@link Supplier} to fetch the value.
     *
     * @param key The NetworkTables key where the boolean value will be published.
     * @param supplier A {@link Supplier} that provides the boolean value to be published.
     */
    public NTSupplierBoolean(String key, Supplier<Boolean> supplier) {
        this.entry = NetworkTableInstance.getDefault().getEntry(key);
        this.supplier = supplier;
    }

    /**
     * Creates a new {@code NTSupplierBoolean} instance that publishes boolean values to the specified 
     * NetworkTables key, using the given {@link Supplier} to fetch the value.
     * @param table
     * @param key The NetworkTables key where the boolean value will be published.
     * @param supplier A {@link Supplier} that provides the boolean value to be published.
     */
    public NTSupplierBoolean(String table, String key, Supplier<Boolean> supplier) {
        this.entry = NetworkTableInstance.getDefault().getTable(table).getEntry(key);
        this.supplier = supplier;
    }

    /**
     * Updates the value in NetworkTables by getting the boolean value from the {@link Supplier}.
     * The value will be set on the specified entry in NetworkTables.
     */
    public void update() {
        entry.setBoolean(supplier.get());
    }
}
