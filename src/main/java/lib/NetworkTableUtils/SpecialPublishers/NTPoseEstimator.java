package lib.NetworkTableUtils.SpecialPublishers;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import lib.NetworkTableUtils.NormalPublishers.NTData;

public class NTPoseEstimator{
    
    private final Map<Integer, NTData<Pose2d>> slots = new HashMap<>();
 
    public NTPoseEstimator(String key, int numSlots){
        for (int i = 0; i < numSlots; i++) {
            slots.put(i, new NTData<>(key + "Slot[" + i + "]/BotPose", Pose2d.struct));
        }
    }

    public void sendEstimator(SwerveDrivePoseEstimator odometer, int slot){

        if (slots.containsKey(slot)) {
            slots.get(slot).sendData(odometer.getEstimatedPosition());
        } else {
            System.out.println("AdvantageOdometer: Slot " + slot + " not found.");
        }
        
    }

}
