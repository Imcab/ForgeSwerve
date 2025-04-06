package lib.Forge.NetworkTableUtils.NetworkCustomPublishers;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import lib.Forge.NetworkTableUtils.NetworkNormalPublishers.NTData;

/**
 * A utility class for publishing pose data from a {@link SwerveDrivePoseEstimator} to NetworkTables.
 * This class manages multiple slots for storing and publishing the estimated robot pose.
 */
public class NTPoseEstimator{
    
    private final Map<Integer, NTData<Pose2d>> slots = new HashMap<>();
 
    /**
     * Creates a new {@code NTPoseEstimator} instance that publishes robot pose data to the specified NetworkTables key.
     * The pose data is stored in multiple slots, each associated with a unique index.
     *
     * @param key The base NetworkTables key used to publish the pose data. Subkeys will be generated for each slot.
     * @param numSlots The number of slots to initialize for storing pose data.
     */
    public NTPoseEstimator(String key, int numSlots){
        for (int i = 0; i < numSlots; i++) {
            slots.put(i, new NTData<>(key + "Slot[" + i + "]/BotPose", Pose2d.struct));
        }
    }

    /**
     * Publishes the estimated robot pose to NetworkTables in the specified slot.
     * The pose is retrieved from the {@link SwerveDrivePoseEstimator} and sent to the corresponding slot.
     * 
     * @param odometer The {@link SwerveDrivePoseEstimator} used to retrieve the estimated robot pose.
     * @param slot The slot index where the pose data will be published.
     */
    public void sendEstimator(SwerveDrivePoseEstimator odometer, int slot){

        if (slots.containsKey(slot)) {
            slots.get(slot).sendData(odometer.getEstimatedPosition());
        } else {
            System.out.println("AdvantageOdometer: Slot " + slot + " not found.");
        }
        
    }

}
