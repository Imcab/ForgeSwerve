package lib.Forge.NetworkTableUtils.NetworkSubsystem.Interfaces;

import edu.wpi.first.wpilibj2.command.Command;
import lib.Forge.NetworkTableUtils.NetworkMultipleData.NTPublisher;
import lib.Forge.NetworkTableUtils.NetworkSubsystem.Interfaces.Annotations.NetworkCommand;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class NetworkCommandRegister{
    
    private NetworkCommandRegister() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static void registerNetworkCommands(String table, Object instance) {
        Class<?> clazz = instance.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(NetworkCommand.class) && Command.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                try {
                    Command command = (Command) field.get(instance);
                    if (command != null) {
                        NetworkCommand annotation = field.getAnnotation(NetworkCommand.class);
                        String path = annotation.value().isEmpty() ? "NetworkCommands/" + field.getName() : annotation.value();
                        NTPublisher.publish(table, path, command);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(NetworkCommand.class) && Command.class.isAssignableFrom(method.getReturnType()) && method.getParameterCount() == 0) {
                method.setAccessible(true);
                try {
                    Command command = (Command) method.invoke(instance);
                    if (command != null) {
                        NetworkCommand annotation = method.getAnnotation(NetworkCommand.class);
                        String path = annotation.value().isEmpty() ? "NetworkCommands/" + method.getName() : annotation.value();
                        NTPublisher.publish(table, path, command);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
