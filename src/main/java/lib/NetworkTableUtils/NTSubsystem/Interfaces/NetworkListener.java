package lib.NetworkTableUtils.NTSubsystem.Interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to mark methods that act as listeners for NetworkTables entries.
 * The {@code key} parameter specifies the NetworkTables key that the method listens for changes on.
 * 
 * <p>This annotation can be applied to methods that are intended to listen for changes on a specific
 * NetworkTables entry. When the value of the entry specified by the {@code key} changes, the annotated
 * method will be invoked.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NetworkListener{

    /**
     * The NetworkTables key that this listener will listen for changes on.
     *
     * @return The key for the NetworkTables entry that the method listens to.
     */
    String key();
}
