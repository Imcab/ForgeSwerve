package lib.Forge.Field;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class AllianceUtil {

    public static boolean isBlue(){
        return DriverStation.getAlliance().
        orElse(Alliance.Blue) == Alliance.Blue;
    }

    public static boolean isRed(){
        return !isBlue();
    }
}
