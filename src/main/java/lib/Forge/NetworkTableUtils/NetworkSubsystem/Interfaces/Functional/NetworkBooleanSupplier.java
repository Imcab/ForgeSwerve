package lib.Forge.NetworkTableUtils.NetworkSubsystem.Interfaces.Functional;

import lib.Forge.NetworkTableUtils.NetworkMultipleData.NTPublisher;

/**
 * A supplier of boolean values.
 *
 * <p>This is a functional interface whose functional method is {@link #getAsBoolean()}.</p>
 */
@FunctionalInterface
public interface NetworkBooleanSupplier {

    /**
     * Gets a boolean value.
     *
     * @return a boolean value
     */
    boolean getAsBoolean();

    /**
     * Publishes the boolean value to NetworkTables.
     *
     * @param table the table to publish to
     * @param key the key to publish to
     */
    default void publish(String table, String key){
        NTPublisher.publish(table, key, getAsBoolean());
    }

    
}
