package lib.Forge.NetworkTableUtils.NetworkSubsystem.Interfaces.Functional;

import lib.Forge.NetworkTableUtils.NetworkMultipleData.NTPublisher;

/**
 * A supplier of double values.
 *
 * <p>This is a functional interface whose functional method is {@link #getAsDouble()}.</p>
 */
@FunctionalInterface
public interface NetworkDoubleSupplier{

    /**
     * Gets a double value.
     *
     * @return a double value
     */
    double getAsDouble();

    /**
     * Publishes the double value to NetworkTables.
     *
     * @param table the table to publish to
     * @param key the key to publish to
     */
    default void publish(String table, String key){
        NTPublisher.publish(table, key, getAsDouble());
    }
}
