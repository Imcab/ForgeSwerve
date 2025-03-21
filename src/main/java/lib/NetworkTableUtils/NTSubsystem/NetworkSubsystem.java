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
        
        if (!registeredPublishers.isEmpty()) {
            System.err.println("[NetworkSubsystem] WARNING: registerAnnotatedPublishers() se ejecutó más de una vez.");
            return;
        }
    
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(AutoNetworkPublisher.class)) continue;
    
            String key = method.getAnnotation(AutoNetworkPublisher.class).key();
            method.setAccessible(true);
    
            Supplier<?> supplier = () -> {
                try { return method.invoke(this); }
                catch (Exception e) { e.printStackTrace(); return null; }
            };
    
            Class<?> returnType = method.getReturnType();
    
            try {
                if (returnType == Pose2d.class) {
                    NTData<Pose2d> ntData = new NTData<>(key, Pose2d.struct);
                    registerPublisher(() -> ntData.sendData((Pose2d) supplier.get()));
                } else if (returnType == Pose3d.class) {
                    NTData<Pose3d> ntData = new NTData<>(key, Pose3d.struct);
                    registerPublisher(() -> ntData.sendData((Pose3d) supplier.get()));
                } else if (returnType == Rotation2d.class) {
                    NTData<Rotation2d> ntData = new NTData<>(key, Rotation2d.struct);
                    registerPublisher(() -> ntData.sendData((Rotation2d) supplier.get()));
                } else if (returnType == Rotation3d.class) {
                    NTData<Rotation3d> ntData = new NTData<>(key, Rotation3d.struct);
                    registerPublisher(() -> ntData.sendData((Rotation3d) supplier.get()));
                } else if (returnType == Translation2d.class) {
                    NTData<Translation2d> ntData = new NTData<>(key, Translation2d.struct);
                    registerPublisher(() -> ntData.sendData((Translation2d) supplier.get()));
                } else if (returnType == Translation3d.class) {
                    NTData<Translation3d> ntData = new NTData<>(key, Translation3d.struct);
                    registerPublisher(() -> ntData.sendData((Translation3d) supplier.get()));
                } else if (returnType == ChassisSpeeds.class) {
                    NTData<ChassisSpeeds> ntData = new NTData<>(key, ChassisSpeeds.struct);
                    registerPublisher(() -> ntData.sendData((ChassisSpeeds) supplier.get()));
                } else if (returnType == SwerveModuleState[].class) {
                    NTArrayData<SwerveModuleState> ntData = new NTArrayData<>(key, SwerveModuleState.struct);
                    registerPublisher(() -> ntData.sendData((SwerveModuleState[]) supplier.get()));
                } else if (returnType == SwerveModulePosition[].class) {
                    NTArrayData<SwerveModulePosition> ntData = new NTArrayData<>(key, SwerveModulePosition.struct);
                    registerPublisher(() -> ntData.sendData((SwerveModulePosition[]) supplier.get()));
                } else if (returnType == double[].class || returnType == Double[].class) {
                    NTSupplierDoubleArray ntData = new NTSupplierDoubleArray(key, () -> (double[]) supplier.get());
                    registerPublisher(ntData::update);
                } else if (returnType == double.class || returnType == Double.class) {
                    NTSupplierDouble ntData = new NTSupplierDouble(key, () -> (Double) supplier.get());
                    registerPublisher(ntData::update);
                } else if (returnType == boolean.class || returnType == Boolean.class) {
                    NTSupplierBoolean ntData = new NTSupplierBoolean(key, () -> (Boolean) supplier.get());
                    registerPublisher(ntData::update);
                } else if (returnType == Boolean[].class || returnType == boolean[].class) {
                    NTSupplierBooleanArray ntData = new NTSupplierBooleanArray(key, () -> (Boolean[]) supplier.get());
                    registerPublisher(ntData::update);
                } else {
                    System.err.println("[AutoNetworkPublisher] Not Supported Data type! " + returnType.getSimpleName());
                }
    
            } catch (Exception e) {
                System.err.println("[AutoNetworkPublisher] Error creando publisher para key: " + key);
                e.printStackTrace();
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
