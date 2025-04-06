package lib.Forge.Math.Controllers;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import lib.Forge.Math.Operator;
import lib.Forge.Math.Constants.ProfileGains.CompleteFeedForwardGains;
import lib.Forge.Math.Constants.ProfileGains.MotionModelGains;
import lib.Forge.Math.Constants.ProfileGains.PIDGains;
import lib.Forge.Math.Constants.ProfileGains.SimpleFeedForwardGains;

public class Control {
 
    @FunctionalInterface
    public interface ControlResult{
        double getOutput();

        default ControlResult plus(ControlResult other){
            return ()-> getOutput() + other.getOutput();
        }

        default ControlResult minus(ControlResult other){
            return ()-> getOutput() - other.getOutput();
        }

        default ControlResult clamp(double min, double max){
            return ()-> Math.max(Math.min(getOutput(), max), min);
        }

        default ControlResult negate(){
            return ()-> -getOutput();
        }

        default ControlResult times(double scalar){
            return ()-> getOutput() * scalar;
        }

        default ControlResult divide(double scalar){
            return ()-> getOutput() / scalar;
        }

        default ControlResult withOperation(Operator.UnaryOperation operation){
            return ()-> operation.apply(getOutput());
        }

        default ControlResult withOperation(Operator.Operation operation, double b){
            return ()-> operation.apply(getOutput(), b);
        }

        default ControlResult withDeadband(double threshold) {
            return () -> Math.abs(getOutput()) > threshold ? getOutput() : 0.0;
        }

        default ControlResult delete(){
            return ()-> 0.0;
        }

    }

    public static abstract class ControlBase<S, M>{

        public abstract ControlResult calculate(S setpoint, M measurement);

        public abstract ControlResult calculate(M measurement);

        public abstract S getSetpoint();

        public abstract void setSetpoint(S setpoint);
    
    }

    public static class FeedForwardControl{

        public static ControlResult calculate(SimpleFeedForwardGains gains, double velocity, double acceleration){
            return ()-> 
            Math.signum(velocity) * gains.kS() +
            gains.kV() * velocity +
            gains.kA() * acceleration;
        }

        public static ControlResult calculate(CompleteFeedForwardGains gains, double velocity, double acceleration){
            return ()-> 
            Math.signum(velocity) * gains.kS() +
            gains.kG() +
            gains.kV() * velocity +
            gains.kA() * acceleration;
        }

    }

    public static class PIDControl extends Control.ControlBase<Double, Double>{

        private PIDGains gains;
        private final PIDController controller;

        public PIDControl(PIDGains gains){
            this.gains = gains;
            controller = new PIDController(gains.kP(), gains.kI(), gains.kD());

        }

        public PIDControl(PIDGains gains, double period){
            this.gains = gains;
            controller = new PIDController(gains.kP(), gains.kI(), gains.kD(), period);
        }

        public void setGains(PIDGains gains){
            this.gains = gains;
            controller.setP(gains.kP());
            controller.setI(gains.kI());
            controller.setD(gains.kD());
        }

        public PIDGains getGains(){
            return gains;
        }

        @Override
        public ControlResult calculate(Double setpoint, Double measurement){
            return ()-> controller.calculate(setpoint, measurement);
        }

        @Override
        public ControlResult calculate(Double measurement){
            return ()-> controller.calculate(measurement);
        }

        @Override
        public Double getSetpoint(){
            return controller.getSetpoint();
        }

        @Override
        public void setSetpoint(Double setpoint){
            controller.setSetpoint(setpoint);
        }

        public void disableContinuousInput(){
            controller.disableContinuousInput();
        }

        public void continuousInput(double minInput, double maxInput){
            controller.enableContinuousInput(minInput, maxInput);
        }

        public void setTolerance(double tolerance){
            controller.setTolerance(tolerance);
        }

        public PIDController getController(){
            return controller;
        }

        public void reset(){
            controller.reset();
        }

    }

    public static class PositionState extends TrapezoidProfile.State {

        public PositionState(double position) {
            super(position, 0);
        }
        
    }

    public static class MotionModelControl extends Control.ControlBase<TrapezoidProfile.State, Double>{

        private final ProfiledPIDController controller;
        private MotionModelGains gains;

        public MotionModelControl(MotionModelGains gains) {

            this.gains = gains;
        
            this.controller = new ProfiledPIDController(
                gains.kP(), gains.kI(), gains.kD(),
                new TrapezoidProfile.Constraints(
                    gains.maxVelocity(), gains.maxAcceleration()));
        }

        public void setGains(MotionModelGains gains){
            controller.setP(gains.kP());
            controller.setI(gains.kI());
            controller.setD(gains.kD());
            controller.setConstraints(new TrapezoidProfile.Constraints(gains.maxVelocity(), gains.maxAcceleration()));
        }

        public MotionModelGains getGains(){
            return gains;
        }

        @Override
        public ControlResult calculate(TrapezoidProfile.State setpoint, Double measurement) {
            return ()-> controller.calculate(measurement, setpoint);
        }

        @Override
        public ControlResult calculate(Double measurement) {
            return ()-> controller.calculate(measurement);
        }

        @Override
        public TrapezoidProfile.State getSetpoint() {
            return controller.getSetpoint();
        }

        @Override
        public void setSetpoint(TrapezoidProfile.State setpoint) {
            controller.setGoal(setpoint);
        }

        public void disableContinuousInput(){
            controller.disableContinuousInput();
        }

        public void continuousInput(double minInput, double maxInput){
            controller.enableContinuousInput(minInput, maxInput);
        }

        public void setTolerance(double tolerance, double velocityTolerance){
            controller.setTolerance(tolerance, velocityTolerance);
        }

        public ProfiledPIDController getController() {
            return controller;
        }

        public void reset(double position, double velocity){
            controller.reset(position, velocity);
        }

    }

    public static class CascadeControl extends Control.ControlBase<Double,Double>{

        private final PIDControl outerLoop;
        private final PIDControl innerLoop;
        private double setpoint;

        public CascadeControl(PIDControl outerLoop, PIDControl innerLoop) {
            this.outerLoop = outerLoop;
            this.innerLoop = innerLoop;
        }

        @Override
        public ControlResult calculate(Double setpoint, Double measurement) {
            this.setpoint = setpoint;

            var intermediateSetpoint = outerLoop.calculate(setpoint, measurement).getOutput();
      
            return innerLoop.calculate(intermediateSetpoint, measurement);
        }

        @Override
        public ControlResult calculate(Double measurement) {
            var intermediateSetpoint = outerLoop.calculate(setpoint, measurement).getOutput();
      
            return innerLoop.calculate(intermediateSetpoint, measurement);
        }

        @Override
        public Double getSetpoint() {
            return setpoint;
        }

        @Override
        public void setSetpoint(Double setpoint) {
            this.setpoint = setpoint;
        }

        public PIDControl getOuterLoop() {
            return outerLoop;
        }

        public PIDControl getInnerLoop() {
            return innerLoop;
        }

        public void reset(){
            outerLoop.reset();
            innerLoop.reset();
        }
      
    }

}
