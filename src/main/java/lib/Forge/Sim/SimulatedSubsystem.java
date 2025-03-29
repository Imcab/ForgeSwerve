package lib.Forge.Sim;

import java.lang.reflect.Field;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.RobotBase;

public interface SimulatedSubsystem{
    
    public default boolean isInSimulation(){
        return RobotBase.isSimulation();
    }

    public void SimulationDevicesPeriodic();

    public void RealDevicesPeriodic();

    public default void handleSubsystemRealityLoop(){

        if (isInSimulation()) {
            SimulationDevicesPeriodic();
        }else{
            RealDevicesPeriodic();
        }
    }

    public default void initializeSubsystemDevices(String key){

        if (isInSimulation()) {
            for (Field field : this.getClass().getDeclaredFields()) {
    
                if (field.isAnnotationPresent(RealDevice.class)) {
                    try {
                        field.setAccessible(true);
                        field.set(this, null);
                    } catch (IllegalAccessException e) {
                        System.err.println("Error at deleting field: " + field.getName());
                    }
                }

                if (field.isAnnotationPresent(SimulatedDevice.class)) {

                    String fieldKey = key + "/Simulated/" + field.getName();
                    NetworkTableInstance.getDefault().
                    getStringTopic(fieldKey).publish();
                }
            }

        }else{
            for (Field field : this.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(SimulatedDevice.class)) {
                    try {
                        field.setAccessible(true);
                        field.set(this, null);
                    } catch (IllegalAccessException e) {
                        System.err.println("Error at deleting field: " + field.getName());
                    }
                }

                if (field.isAnnotationPresent(RealDevice.class)) {
                    String fieldKey = key + "/Real/" + field.getName();
                    NetworkTableInstance.getDefault().
                    getStringTopic(fieldKey).publish();
            }
            }
        }
    }
}
