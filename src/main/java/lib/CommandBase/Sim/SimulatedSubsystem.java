package lib.CommandBase.Sim;

import java.lang.reflect.Field;

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

    public default void initializeSubsystemDevices(){
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
            }
        }
    }
}
