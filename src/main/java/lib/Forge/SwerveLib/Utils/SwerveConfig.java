package lib.Forge.SwerveLib.Utils;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;

public class SwerveConfig{
    
    public static class SingleMotorGearBoxes{
        
        public static DCMotor kNEO = DCMotor.getNEO(1);
        public static DCMotor kKRAKEN = DCMotor.getKrakenX60(1);

    }

    public record ModuleReductions(double driveReduction, double turnReduction) {}

    public record Wheel(double radiusInches) {
        public double radiusMeters(){return Units.inchesToMeters(radiusInches);}

        public double diameterMeters(){return radiusMeters() * 2;}

        public double diameterInches(){return radiusInches * 2;}
    }

    public record ModuleCurrentLimits(int driveCurrentAmps, int turnCurrentAmps) {}
    
}
