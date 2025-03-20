package lib.NetworkTableUtils.NTSubsystem;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEvent;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import lib.NetworkTableUtils.NTSubsystem.Interfaces.*;
import lib.NetworkTableUtils.NormalPublishers.*;
import lib.NetworkTableUtils.SupplierPublishers.*;

public abstract class NetworkSubsystem extends SubsystemBase {

    private static final Map<String, Boolean> triggerState = new ConcurrentHashMap<>();
    private static final Set<Runnable> registeredPublishers = new HashSet<>();

    public NetworkSubsystem() {
        registerNetworkTriggers();
        registerAnnotatedPublishers();
        registerNetworkCommands();
    }

    private void registerNetworkCommands() {
        for (Method method : this.getClass().getDeclaredMethods()) {
        if (method.isAnnotationPresent(NetworkCommand.class)) {
            NetworkCommand annotation = method.getAnnotation(NetworkCommand.class);
            String key = annotation.key();

            NetworkTableInstance instance = NetworkTableInstance.getDefault();
            instance.getEntry(key).setDefaultBoolean(false);

            instance.addListener(instance.getEntry(key), EnumSet.of(NetworkTableEvent.Kind.kValueAll), event -> {
                if (event.valueData.value.getBoolean()) {
                    try {
                        method.invoke(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    instance.getEntry(key).setBoolean(false);
                    }
                });
            }
        }
    }

    private void registerNetworkTriggers() {
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(NetworkListener.class)) {
                NetworkListener annotation = method.getAnnotation(NetworkListener.class);
                String key = annotation.key();
                triggerState.put(key, true);

                NetworkTableInstance instance = NetworkTableInstance.getDefault();
                instance.addListener(
                        instance.getEntry(key),
                        EnumSet.of(NetworkTableEvent.Kind.kValueAll),
                        event -> {
                            try {
                                if (!triggerState.getOrDefault(key, true)) return;
                                Object param = null;
                                if (method.getParameterCount() == 1) {
                                    Class<?> paramType = method.getParameterTypes()[0];
                                    if (paramType == Double.class || paramType == double.class) {
                                        param = event.valueData.value.getDouble();
                                    } else if (paramType == Boolean.class || paramType == boolean.class) {
                                        param = event.valueData.value.getBoolean();
                                    }
                                }
                                if (param != null) method.invoke(this, param);
                                else method.invoke(this);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                );
            }
        }
    }

    private void registerAnnotatedPublishers() {

        clearPublishers();

        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(AutoNetworkPublisher.class)) {
                String key = method.getAnnotation(AutoNetworkPublisher.class).key();
                method.setAccessible(true);

                Supplier<?> supplier = () -> {
                    try { return method.invoke(this); } 
                    catch (Exception e) { e.printStackTrace(); return null; }
                };

                Class<?> returnType = method.getReturnType();
                if (returnType == Pose3d.class) registerPublisher(() -> new NTData<>(key, Pose3d.struct).sendData((Pose3d) supplier.get()));
                else if (returnType == Pose2d.class) registerPublisher(() -> new NTData<>(key, Pose2d.struct).sendData((Pose2d) supplier.get()));
                else if (returnType == Rotation2d.class) registerPublisher(() -> new NTData<>(key, Rotation2d.struct).sendData((Rotation2d) supplier.get()));
                else if (returnType == Rotation3d.class) registerPublisher(() -> new NTData<>(key, Rotation3d.struct).sendData((Rotation3d) supplier.get()));
                else if (returnType == Translation2d.class) registerPublisher(() -> new NTData<>(key, Translation2d.struct).sendData((Translation2d) supplier.get()));
                else if (returnType == Translation3d.class) registerPublisher(() -> new NTData<>(key, Translation3d.struct).sendData((Translation3d) supplier.get()));
                else if (returnType == ChassisSpeeds.class) registerPublisher(() -> new NTData<>(key, ChassisSpeeds.struct).sendData((ChassisSpeeds) supplier.get()));
                else if (returnType == SwerveModulePosition[].class)registerPublisher(()-> new NTArrayData<>(key, SwerveModulePosition.struct).sendData((SwerveModulePosition[]) supplier.get()));
                else if (returnType == SwerveModuleState[].class) registerPublisher(() -> new NTArrayData<>(key, SwerveModuleState.struct).sendData((SwerveModuleState[]) supplier.get()));
                else if (returnType == Double[].class || returnType == double[].class) registerPublisher(() -> new NTSupplierDoubleArray(key, () -> (double[]) supplier.get()).update());
                else if (returnType == Double.class || returnType == double.class) registerPublisher(() -> new NTSupplierDouble(key, () -> (Double) supplier.get()).update());
                else if (returnType == Boolean.class || returnType == boolean.class) registerPublisher(() -> new NTSupplierBoolean(key, () -> (Boolean) supplier.get()).update());
                else if (returnType == Boolean[].class || returnType == boolean[].class) registerPublisher(() -> new NTSupplierBooleanArray(key, () -> (Boolean[]) supplier.get()).update());
                else System.err.println("[AutoNetworkPublisher] Not Supported Data type! " + returnType.getSimpleName());
            }
        }
    }

    private void registerPublisher(Runnable publisher) {
        registeredPublishers.add(publisher);
    }

    public void clearPublishers(){
        registeredPublishers.clear();
    }

    @Override
    public final void periodic() {
        for (Runnable publisher : registeredPublishers) publisher.run();

        NetworkPeriodic();
    }

    public void NetworkPeriodic(){}

    public void disableTrigger(String key){
        triggerState.put(key, false);
    }

    public void enableTrigger(String key){
        triggerState.put(key, true);
    }

    public void disableAllTriggers(){
        triggerState.replaceAll((k, v) -> false);
    }

    public void enableAllTriggers(){
        triggerState.replaceAll((k, v) -> true);
    }
}
