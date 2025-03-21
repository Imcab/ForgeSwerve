package lib.SwerveLib.SwerveModule.Templates;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import lib.Constants.ProfileGains.PIDFGains;
import lib.Constants.ProfileGains.PIDGains;
import lib.SwerveLib.SwerveModule.SimpleSwerveModule;

public class ModuleTemplate extends SimpleSwerveModule{
    
        private static double WheelRadiusInMeters = Units.inchesToMeters(2); //Change to your actual value
        private static double driveReduction = 0; //Change to your actual value
        private static double turnReduction = 0; //Change to your actual value

        private static ModuleSpecifications specifications = new ModuleSpecifications(WheelRadiusInMeters, turnReduction, driveReduction);

        private static PIDFGains driveGains = new PIDFGains(0, 0, 0, 0, 0); //Change to your actual values
        private static PIDGains turnGains = new PIDGains(0, 0, 0); //Change to your actual values

        private static boolean publishGainsToDashboard = false; //True if you want to send gains to the dashboard
        private static boolean publishModuleToDashboard = true;

        public ModuleTemplate(int index){
            super(index, specifications, driveGains, turnGains, publishGainsToDashboard, publishModuleToDashboard);
        }

        @Override
        public void motorsConfiguration(){
            //All your motor configurations here, use super.configAccessor for getting motor ID's, inverted Values and offsets
        }

        @Override
        public void FrontLeftConfiguration(HardwareConfiguration config){
            //HardwareConfiguration here, change to your actual values

            config.drivePort = 0;
            config.driveMotorInverted = false;

            config.turnPort = 0;
            config.turnMotorInverted = false;

            config.encoderPort = 0;
            config.turnEncoderOffset = 0;

            config.modulePosition = new Translation2d(0,0);
        }

        @Override
        public void FrontRightConfiguration(HardwareConfiguration config){
            //HardwareConfiguration here, change to your actual values

            config.drivePort = 0;
            config.driveMotorInverted = false;

            config.turnPort = 0;
            config.turnMotorInverted = false;

            config.encoderPort = 0;
            config.turnEncoderOffset = 0;

            config.modulePosition = new Translation2d(0,0);
        }

        @Override
        public void BackLeftConfiguration(HardwareConfiguration config){
            //HardwareConfiguration here, change to your actual values

            config.drivePort = 0;
            config.driveMotorInverted = false;

            config.turnPort = 0;
            config.turnMotorInverted = false;

            config.encoderPort = 0;
            config.turnEncoderOffset = 0;

            config.modulePosition = new Translation2d(0,0);
        }

        @Override
        public void BackRightConfiguration(HardwareConfiguration config){
            //HardwareConfiguration here, change to your actual values

            config.drivePort = 0;
            config.driveMotorInverted = false;

            config.turnPort = 0;
            config.turnMotorInverted = false;

            config.encoderPort = 0;
            config.turnEncoderOffset = 0;

            config.modulePosition = new Translation2d(0,0);
        }

        @Override
        public void setDriveVoltage(double volts){
            //Function to supply a given voltage to your driveMotor.
        }

        @Override
        public void setTurnVoltage(double volts){
            //Function to supply a given voltage to your turnMotor.
        }

        @Override
        public Rotation2d getModuleAngle(){
            //Get your module angle (in radians)
            return new Rotation2d(); //Actual method here
        }

        @Override
        public double getRawDriveEncoderVelocity(){
            //Your raw encoder velocity without any conversions, gear reductions etc.
            return 0; //Actual method here
        }

        @Override
        public double getRawTurnEncoderVelocity(){
            //Your raw encoder velocity without any conversions, gear reductions etc.
            return 0; //Actual method here
        }

}
