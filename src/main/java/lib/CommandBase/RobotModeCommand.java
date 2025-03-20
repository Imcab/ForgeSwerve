package lib.CommandBase;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * A base command that adapts its behavior based on the current robot mode (Autonomous or Teleoperated).
 * <p>
 * This abstract class enforces the separation of lifecycle methods between Autonomous and Teleoperated modes.
 * Subclasses must implement mode-specific methods rather than overriding the standard WPILib
 * {@code Command} lifecycle methods directly.
 * </p>
 * 
 * <h2>Usage:</h2>
 * <ul>
 *     <li>Override {@code autonomousInitialize()}, {@code autonomousExecute()}, and {@code autonomousEnd()} for autonomous behavior.</li>
 *     <li>Override {@code teleopInitialize()}, {@code teleopExecute()}, and {@code teleopEnd()} for teleoperated behavior.</li>
 *     <li>Define termination conditions separately with {@code autonomousEndCondition()} and {@code teleopEndCondition()}.</li>
 * </ul>
 * 
 * <h2>Note:</h2>
 * <ul>
 *     <li>Do <b>not</b> override {@code initialize()}, {@code execute()}, {@code end()}, or {@code isFinished()}.</li>
 *     <li>The class determines the robot mode automatically using {@link DriverStation#isAutonomous()}.</li>
 * </ul>
 * 
 * @author [frc #3472]
 */
public abstract class RobotModeCommand extends Command{

    private boolean isAutonomous(){
        return DriverStation.isAutonomous();
    }

    /**
     * Checks if the autonomous command should end.
     * Subclasses must implement this method to define the autonomous termination condition.
     *
     * @return {@code true} if the autonomous command should finish, {@code false} otherwise.
     */
    public abstract boolean autonomousEndCondition();

     /**
     * Checks if the teleoperated command should end.
     * Subclasses must implement this method to define the teleop termination condition.
     *
     * @return {@code true} if the teleop command should finish, {@code false} otherwise.
     */
    public abstract boolean teleopEndCondition();

     /**
     * Called once when the command starts in teleoperated mode.
     * Subclasses must implement this to define initialization logic for teleop.
     */
    public abstract void teleopInitialize();

    /**
     * Called once when the command starts in autonomous mode.
     * Subclasses must implement this to define initialization logic for autonomous.
     */
    public abstract void autonomousInitialize();

    /**
     * Called repeatedly while the command is active in teleoperated mode.
     * Subclasses must implement this to define execution logic for teleop.
     */
    public abstract void teleopExecute();

    /**
     * Called repeatedly while the command is active in autonomous mode.
     * Subclasses must implement this to define execution logic for autonomous.
     */
    public abstract void autonomousExecute();

    /**
     * Called once when the command ends in teleoperated mode.
     * Subclasses must implement this to define cleanup logic for teleop.
     */
    public abstract void teleopEnd();

    /**
     * Called once when the command ends in autonomous mode.
     * Subclasses must implement this to define cleanup logic for autonomous.
     */
    public abstract void autonomousEnd();
    
    /**
     * Initializes the command based on the current robot mode.
     * This method should not be overridden; instead, implement {@code autonomousInitialize()} or {@code teleopInitialize()}.
     */
    @Override
    public final void initialize(){
        if (isAutonomous()) {
            autonomousInitialize();
        }else{
            teleopInitialize();
        }
    }

    /**
     * Executes the command based on the current robot mode.
     * This method should not be overridden; instead, implement {@code autonomousExecute()} or {@code teleopExecute()}.
     */
    @Override
    public final void execute(){
        if (isAutonomous()) {
            autonomousExecute();
        }else{
            teleopExecute();
        }
    }

     /**
     * Ends the command based on the current robot mode.
     * This method should not be overridden; instead, implement {@code autonomousEnd()} or {@code teleopEnd()}.
     *
     * @param interrupted {@code true} if the command was interrupted, {@code false} if it ended normally.
     */
    @Override
    public final void end(boolean interrupted){
        if (isAutonomous()) {
            autonomousEnd();
        }else{
            teleopEnd();
        }
    }

    /**
     * Determines whether the command has finished based on the current robot mode.
     * This method should not be overridden; instead, implement {@code autonomousEndCondition()} or {@code teleopEndCondition()}.
     *
     * @return {@code true} if the command should end, {@code false} otherwise.
     */
    @Override
    public final boolean isFinished(){
        
        return isAutonomous() ? autonomousEndCondition(): teleopEndCondition();
    }

}
