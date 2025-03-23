package lib.NetworkTableUtils.NTSubsystem;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.*;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.util.Color;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

import lib.NetworkTableUtils.MultipleData.NTPublisher;
import lib.NetworkTableUtils.NTSubsystem.Interfaces.*;
import lib.NetworkTableUtils.NormalPublishers.*;
import lib.NetworkTableUtils.SupplierPublishers.*;

public abstract class NetworkSubsystem extends SubsystemBase {

    private static final Set<Runnable> registeredPublishers = new HashSet<>();
    private final String tableName;

    public NetworkSubsystem(String tableName, boolean subsystemInfo) {
        this.tableName = tableName;
        registerAnnotatedPublishers();
        NetworkCommandRegister.registerNetworkCommands(tableName, this);
        NTPublisher.publish(tableName, "TableKey", getTableKey());

        if (subsystemInfo) {
            NTPublisher.publish(tableName, "Subsystem", this);
        }
        
    }

    public String getTableKey(){
        return tableName;
    }

    private void registerAnnotatedPublishers() {

        clearPublishers();
        
        if (!registeredPublishers.isEmpty()) {
            System.err.println("[NetworkSubsystem] WARNING: registerAnnotatedPublishers() Overloop!");
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
                    NTData<Pose2d> ntData = new NTData<>(tableName,key, Pose2d.struct);
                    registerPublisher(() -> ntData.sendData((Pose2d) supplier.get()));

                } else if (returnType == Pose2d[].class) {
                    NTArrayData<Pose2d> ntData = new NTArrayData<>(tableName,key, Pose2d.struct);
                    registerPublisher(() -> ntData.sendData((Pose2d[]) supplier.get()));

                } else if (returnType == Pose3d.class) {
                    NTData<Pose3d> ntData = new NTData<>(tableName,key, Pose3d.struct);
                    registerPublisher(() -> ntData.sendData((Pose3d) supplier.get()));

                } else if (returnType == Pose3d[].class) {
                    NTArrayData<Pose3d> ntData = new NTArrayData<>(tableName,key, Pose3d.struct);
                    registerPublisher(() -> ntData.sendData((Pose3d[]) supplier.get()));

                } else if (returnType == Rotation2d.class) {
                    NTData<Rotation2d> ntData = new NTData<>(tableName,key, Rotation2d.struct);
                    registerPublisher(() -> ntData.sendData((Rotation2d) supplier.get()));

                } else if (returnType == Rotation2d[].class) {
                    NTArrayData<Rotation2d> ntData = new NTArrayData<>(tableName,key, Rotation2d.struct);
                    registerPublisher(() -> ntData.sendData((Rotation2d[]) supplier.get()));
                
                } else if (returnType == Rotation3d.class) {
                    NTData<Rotation3d> ntData = new NTData<>(tableName,key, Rotation3d.struct);
                    registerPublisher(() -> ntData.sendData((Rotation3d) supplier.get()));

                } else if (returnType == Translation2d.class) {
                    NTData<Translation2d> ntData = new NTData<>(tableName,key, Translation2d.struct);
                    registerPublisher(() -> ntData.sendData((Translation2d) supplier.get()));

                }else if(returnType == Translation2d[].class){
                    NTArrayData<Translation2d> ntData = new NTArrayData<>(tableName,key, Translation2d.struct);
                    registerPublisher(()-> ntData.sendData((Translation2d[]) supplier.get()));

                }
                else if (returnType == Translation3d.class) {
                    NTData<Translation3d> ntData = new NTData<>(tableName,key, Translation3d.struct);
                    registerPublisher(() -> ntData.sendData((Translation3d) supplier.get()));

                }
                else if(returnType == Translation3d[].class){
                NTArrayData<Translation3d> ntData = new NTArrayData<>(tableName,key, Translation3d.struct);
                registerPublisher(()-> ntData.sendData((Translation3d[]) supplier.get()));

                }
                else if (returnType == ChassisSpeeds.class) {
                    NTData<ChassisSpeeds> ntData = new NTData<>(tableName,key, ChassisSpeeds.struct);
                    registerPublisher(() -> ntData.sendData((ChassisSpeeds) supplier.get()));

                } else if (returnType == SwerveModuleState[].class) {
                    NTArrayData<SwerveModuleState> ntData = new NTArrayData<>(tableName,key, SwerveModuleState.struct);
                    registerPublisher(() -> ntData.sendData((SwerveModuleState[]) supplier.get()));

                } else if (returnType == SwerveModulePosition[].class) {
                    NTArrayData<SwerveModulePosition> ntData = new NTArrayData<>(tableName,key, SwerveModulePosition.struct);
                    registerPublisher(() -> ntData.sendData((SwerveModulePosition[]) supplier.get()));

                } else if (returnType == double[].class || returnType == Double[].class) {
                    NTSupplierDoubleArray ntData = new NTSupplierDoubleArray(tableName,key, () -> (double[]) supplier.get());
                    registerPublisher(ntData::update);

                } else if (returnType == double.class || returnType == Double.class) {
                    NTSupplierDouble ntData = new NTSupplierDouble(tableName,key, () -> (Double) supplier.get());
                    registerPublisher(ntData::update);
                    
                } else if (returnType == boolean.class || returnType == Boolean.class) {
                    NTSupplierBoolean ntData = new NTSupplierBoolean(tableName,key, () -> (Boolean) supplier.get());
                    registerPublisher(ntData::update);

                } else if (returnType == Boolean[].class || returnType == boolean[].class) {
                    NTSupplierBooleanArray ntData = new NTSupplierBooleanArray(tableName,key, () -> (Boolean[]) supplier.get());
                    registerPublisher(ntData::update);
                } else {
                    System.err.println("[AutoNetworkPublisher] Not Supported Data type! " + returnType.getSimpleName());
                }
    
            } catch (Exception e) {
                System.err.println("[AutoNetworkPublisher] Error creating publisher at key: " + key);
                e.printStackTrace();
            }
        }
    }
    
    private void registerPublisher(Runnable publisher) {
        registeredPublishers.add(publisher);
    }

    public final void publishOutput(String key, double value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, double[] value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, boolean value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, boolean[] value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, String value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, String[] value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Color value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Pose2d value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Pose2d[] value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Pose3d value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Pose3d[] value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Rotation2d value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Rotation2d[] value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Rotation3d value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Rotation3d[] value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Translation2d value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Translation2d[] value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Translation3d value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Translation3d[] value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Transform2d value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Transform2d[] value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Transform3d value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Transform3d[] value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, SwerveModulePosition value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, SwerveModulePosition[] value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, SwerveModuleState value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, SwerveModuleState[] value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, ChassisSpeeds value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, ChassisSpeeds[] value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public final void publishOutput(String key, Sendable value){
        NTPublisher.publish(getTableKey(), key, value);
    }

    public double getOutput(String key, double defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public double[] getOutput(String key, double[] defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public boolean getOutput(String key, boolean defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public boolean[] getOutput(String key, boolean[] defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public String getOutput(String key, String defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public String[] getOutput(String key, String[] defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public Color getOutput(String key, Color defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public Pose2d getOutput(String key, Pose2d defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public Pose2d[] getOutput(String key, Pose2d[] defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public Pose3d getOutput(String key, Pose3d defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public Pose3d[] getOutput(String key, Pose3d[] defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public Rotation2d getOutput(String key, Rotation2d defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public Rotation2d[] getOutput(String key, Rotation2d[] defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public Rotation3d getOutput(String key, Rotation3d defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public Rotation3d[] getOutput(String key, Rotation3d[] defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public Translation2d getOutput(String key, Translation2d defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public Translation2d[] getOutput(String key, Translation2d[] defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public Translation3d getOutput(String key, Translation3d defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public Translation3d[] getOutput(String key, Translation3d[] defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public Transform2d getOutput(String key, Transform2d defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public Transform2d[] getOutput(String key, Transform2d[] defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public Transform3d getOutput(String key, Transform3d defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public Transform3d[] getOutput(String key, Transform3d[] defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public SwerveModulePosition getOutput(String key, SwerveModulePosition defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public SwerveModulePosition[] getOutput(String key, SwerveModulePosition[] defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public SwerveModuleState getOutput(String key, SwerveModuleState defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public SwerveModuleState[] getOutput(String key, SwerveModuleState[] defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public ChassisSpeeds getOutput(String key, ChassisSpeeds defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

    public ChassisSpeeds[] getOutput(String key, ChassisSpeeds[] defaultValue){
        return NTPublisher.retrieve(getTableKey(), key, defaultValue);
    }

   
    public void clearPublishers(){
        registeredPublishers.clear();
    }

    @Override
    public final void periodic() {
        for (Runnable publisher : registeredPublishers) publisher.run();

        NetworkPeriodic();
    }

    public abstract void NetworkPeriodic();

  
}
