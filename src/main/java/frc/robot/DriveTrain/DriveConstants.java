package frc.robot.DriveTrain;

public class DriveConstants {  
    
    public static final class frontLeft{

        public static final int DrivePort = 1; 
        public static final int TurnPort = 2; 
        public static final int EncPort = 19;
        public static final double offset = 0.08;                                                                                                                                                                                                                                                                                                                                                                                                                                                                        ; //48     //93  //138      //48 o 138 o 228
 
        public static final boolean DrivemotorReversed = true;
        public static final boolean TurnmotorReversed = true;

    }

    public static final class frontRight{

        public static final int DrivePort = 4; 
        public static final int TurnPort = 3; 
        public static final int EncPort = 20; 
        public static final double offset = -0.47; 
 
        public static final boolean DrivemotorReversed = true;
        public static final boolean TurnmotorReversed = true;

    }

    public static final class backLeft{

        public static final int DrivePort = 5; 
        public static final int TurnPort = 6; 
        public static final int EncPort = 21; 
        public static final double offset = 0.49;
 
        public static final boolean DrivemotorReversed = true;
        public static final boolean TurnmotorReversed = true; 

    }

    public static final class backRight{

        public static final int DrivePort = 7; 
        public static final int TurnPort = 8; 
        public static final int EncPort = 22; 
        public static final double offset = -0.43; 
 
        public static final boolean DrivemotorReversed = true;
        public static final boolean TurnmotorReversed = true;

    }
}
