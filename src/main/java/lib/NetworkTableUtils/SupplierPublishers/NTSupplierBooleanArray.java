package lib.NetworkTableUtils.SupplierPublishers;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;
import java.util.function.Supplier;

public class NTSupplierBooleanArray{
    private final NetworkTableEntry entry;
    private final Supplier<Boolean[]> supplier;

    public NTSupplierBooleanArray(String key, Supplier<Boolean[]> supplier) {
        this.entry = NetworkTableInstance.getDefault().getTable("NTSubsystem").getEntry(key);
        this.supplier = supplier;
    }

    public void update() {
        entry.setBooleanArray(supplier.get());
    }
}
