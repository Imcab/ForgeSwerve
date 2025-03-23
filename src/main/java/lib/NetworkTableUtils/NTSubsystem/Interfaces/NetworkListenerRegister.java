package lib.NetworkTableUtils.NTSubsystem.Interfaces;

import edu.wpi.first.networktables.*;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class NetworkListenerRegister{
    private static final NetworkTableInstance ntInstance = NetworkTableInstance.getDefault();
    private static final Map<String, Integer> listenerHandles = new HashMap<>();

    private NetworkListenerRegister() {
        throw new UnsupportedOperationException("This is a utility class");
    }

    public static void registerListeners(Object instance) {
        Class<?> clazz = instance.getClass(); //Obtiene la klase, el subsistema

        //Por todos los metodos en la klase checa si tiene un método la anotacion @NetworkListener 
        // y tmb checa que solo tenga un parametro:

        /*
            busca algo como:

            @NetworkListener(key = "NT/Nose/unDouble")
            public void checarCambiosen(double ultimo cambio){}
         */
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(NetworkListener.class) && method.getParameterCount() == 1) {
                method.setAccessible(true); // si el metodo estaba en private lo hace public
                String path = method.getAnnotation(NetworkListener.class).key();
                NetworkTableEntry entry = ntInstance.getEntry(path); // obtieneel path completo para ntTables

                Class<?> paramType = method.getParameterTypes()[0]; 
                //Clase ? porq no sabemos q parametro tiene el metodo, esto porq soporta doubles, booleanos y strings

                //Aqui la función q hara cuando detecte un cambio
                Consumer<NetworkTableValue> callback = value -> {
                    try {

                        //Busca el parametro que tiene el método y recibe el valor adecuado desde la NT
                        if (paramType == Double.class || paramType == double.class) {

                            method.invoke(instance, value.getDouble()); //el invoke es llamar la función del metodo
                        } else if (paramType == Boolean.class || paramType == boolean.class) {
                            method.invoke(instance, value.getBoolean()); // y el value es el parametro q le manda a la funcion
                        } else if (paramType == String.class) {
                            method.invoke(instance, value.getString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace(); // si para un error pos lo imprime haha xddddd
                    }
                };

                //El listener d las NT para escuchar cambios en una entry: kValueAll (cambios locales o de red)
                //Manda el ultimo cambio a la funcion callback d arriba y callback procede a ver que tipo de dato es y
                //hacer la operacion
                int handle = ntInstance.addListener(
                    entry,
                    EnumSet.of(NetworkTableEvent.Kind.kValueAll), 
                    event -> callback.accept(event.valueData.value)
                );
                listenerHandles.put(path, handle);
            }
        }
    }

    public static void unregisterListeners(){
        listenerHandles.forEach((path, handle) -> ntInstance.removeListener(handle));
        listenerHandles.clear();
    }
}
