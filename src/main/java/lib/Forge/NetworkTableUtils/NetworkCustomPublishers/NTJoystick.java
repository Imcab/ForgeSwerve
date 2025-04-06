package lib.Forge.NetworkTableUtils.NetworkCustomPublishers;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/**
 * A utility class for publishing joystick values (from all joystick types) to NetworkTables.
 * This class publishes various joystick axes, button states, and POV hat states.
 */
public class NTJoystick implements Sendable{

    private DoubleSupplier[] rightAxis = new DoubleSupplier[2];
    private DoubleSupplier[] leftAxis = new DoubleSupplier[2];
    private DoubleSupplier[] triggers = new DoubleSupplier[2];
    private BooleanSupplier[] buttons = new BooleanSupplier[4];
    private BooleanSupplier[] bumpers  = new BooleanSupplier[2];
    private BooleanSupplier[] specialButtons = new BooleanSupplier[2];
    private BooleanSupplier[] stickPressed = new BooleanSupplier[2];
    private BooleanSupplier[] povs = new BooleanSupplier[4];

    private String[] buttonNames = new String[4];
    private String[] specialButtonNames = new String[2];

    private NTJoystick() {}

    private NTJoystick(CommandXboxController controller){

        this.rightAxis[0] = ()-> controller.getRightY();
        this.rightAxis[1] = ()-> controller.getRightX();

        this.leftAxis[0] = ()-> controller.getLeftY();
        this.leftAxis[1] = ()-> controller.getLeftX();

        this.triggers[0] = ()->controller.getLeftTriggerAxis();
        this.triggers[1] = ()-> controller.getRightTriggerAxis();

        this.buttons[0] = ()-> controller.a().getAsBoolean();
        this.buttons[1] = ()-> controller.b().getAsBoolean();
        this.buttons[2] = ()-> controller.x().getAsBoolean();
        this.buttons[3] = ()-> controller.y().getAsBoolean();

        this.bumpers[0] = ()-> controller.leftBumper().getAsBoolean();
        this.bumpers[1] = ()-> controller.rightBumper().getAsBoolean();

        this.specialButtons[0] = ()-> controller.start().getAsBoolean();
        this.specialButtons[1] = ()-> controller.back().getAsBoolean();

        this.stickPressed[0] = ()-> controller.leftStick().getAsBoolean();
        this.stickPressed[1] = ()-> controller.rightStick().getAsBoolean();

        this.povs[0] = ()-> controller.povDown().getAsBoolean();
        this.povs[1] = ()-> controller.povLeft().getAsBoolean();
        this.povs[2] = ()-> controller.povRight().getAsBoolean();
        this.povs[3] = ()-> controller.povUp().getAsBoolean();

        this.buttonNames[0] = "A";
        this.buttonNames[1] = "B";
        this.buttonNames[2] = "X";
        this.buttonNames[3] = "Y";

        this.specialButtonNames[0] = "Start";
        this.specialButtonNames[1] = "Back";

    }

    private NTJoystick(CommandPS5Controller controller){

        this.rightAxis[0] = ()-> controller.getRightY();
        this.rightAxis[1] = ()->controller.getRightX();

        this.leftAxis[0] = ()->controller.getLeftY();
        this.leftAxis[1] = ()-> controller.getLeftX();

        this.triggers[0] = ()-> controller.getL2Axis();
        this.triggers[1] = ()-> controller.getR2Axis();

        this.buttons[0] = ()-> controller.cross().getAsBoolean();
        this.buttons[1] = ()-> controller.circle().getAsBoolean();
        this.buttons[2] = ()-> controller.square().getAsBoolean();
        this.buttons[3] = ()-> controller.triangle().getAsBoolean();

        this.bumpers[0] = ()-> controller.L1().getAsBoolean();
        this.bumpers[1] = ()-> controller.R1().getAsBoolean();

        this.specialButtons[0] = ()-> controller.options().getAsBoolean();
        this.specialButtons[1] = ()-> controller.create().getAsBoolean();

        this.stickPressed[0] = ()->controller.L3().getAsBoolean();
        this.stickPressed[1] = ()-> controller.R3().getAsBoolean();

        this.povs[0] = ()-> controller.povDown().getAsBoolean();
        this.povs[1] = ()-> controller.povLeft().getAsBoolean();
        this.povs[2] = ()-> controller.povRight().getAsBoolean();
        this.povs[3] = ()-> controller.povUp().getAsBoolean();

        this.buttonNames[0] = "Cross";
        this.buttonNames[1] = "Circle";
        this.buttonNames[2] = "Square";
        this.buttonNames[3] = "Triangle";

        this.specialButtonNames[0] = "Options";
        this.specialButtonNames[1] = "Create";

    }

    private NTJoystick(CommandPS4Controller controller){

        this.rightAxis[0] = ()-> controller.getRightY();
        this.rightAxis[1] = ()-> controller.getRightX();

        this.leftAxis[0] = ()-> controller.getLeftY();
        this.leftAxis[1] = ()-> controller.getLeftX();

        this.triggers[0] = ()-> controller.getL2Axis();
        this.triggers[1] = ()-> controller.getR2Axis();

        this.buttons[0] = ()-> controller.cross().getAsBoolean();
        this.buttons[1] = ()-> controller.circle().getAsBoolean();
        this.buttons[2] = ()-> controller.square().getAsBoolean();
        this.buttons[3] = ()-> controller.triangle().getAsBoolean();

        this.bumpers[0] = ()-> controller.L1().getAsBoolean();
        this.bumpers[1] = ()-> controller.R1().getAsBoolean();

        this.specialButtons[0] = ()-> controller.options().getAsBoolean();
        this.specialButtons[1] = ()-> controller.share().getAsBoolean();

        this.stickPressed[0] = ()-> controller.L3().getAsBoolean();
        this.stickPressed[1] = ()-> controller.R3().getAsBoolean();

        this.povs[0] = ()-> controller.povDown().getAsBoolean();
        this.povs[1] = ()-> controller.povLeft().getAsBoolean();
        this.povs[2] = ()-> controller.povRight().getAsBoolean();
        this.povs[3] = ()-> controller.povUp().getAsBoolean();

        this.buttonNames[0] = "Cross";
        this.buttonNames[1] = "Circle";
        this.buttonNames[2] = "Square";
        this.buttonNames[3] = "Triangle";

        this.specialButtonNames[0] = "Options";
        this.specialButtonNames[1] = "Share";
    }

    private NTJoystick(DoubleSupplier[] rightAxis, DoubleSupplier[] leftAxis, DoubleSupplier[] triggers, BooleanSupplier[] buttons, BooleanSupplier[] bumpers, BooleanSupplier[] specialButtons, BooleanSupplier[] stickPressed, BooleanSupplier[] povs, String[] buttonNames, String[] specialButtonNames){
        this.rightAxis = rightAxis;
        this.leftAxis = leftAxis;
        this.triggers = triggers;
        this.buttons = buttons;
        this.bumpers = bumpers;
        this.specialButtons = specialButtons;
        this.stickPressed = stickPressed;
        this.povs = povs;
        this.buttonNames = buttonNames;
        this.specialButtonNames = specialButtonNames;
    }


    @Override
    public void initSendable(SendableBuilder builder){

        builder.setSmartDashboardType("Joystick");

        builder.addDoubleProperty("Axis/Right Axis/Y",  rightAxis[0], null);
        builder.addDoubleProperty("Axis/Right Axis/X",  rightAxis[1], null);

        builder.addDoubleProperty("Axis/Left Axis/Y",  leftAxis[0], null);
        builder.addDoubleProperty("Axis/Left Axis/X",  leftAxis[1], null);

        builder.addDoubleProperty("Triggers/Left",  triggers[0], null);
        builder.addDoubleProperty("Triggers/Right",  triggers[1], null);

        builder.addBooleanProperty("Buttons/" + buttonNames[0],  buttons[0], null);
        builder.addBooleanProperty("Buttons/" + buttonNames[1],  buttons[1], null);
        builder.addBooleanProperty("Buttons/" + buttonNames[2],  buttons[2], null);
        builder.addBooleanProperty("Buttons/" + buttonNames[3],  buttons[3], null);

        builder.addBooleanProperty("Bumpers/Left",  bumpers[0], null);
        builder.addBooleanProperty("Bumpers/Right",  bumpers[1], null);

        builder.addBooleanProperty("Special Buttons/" + specialButtonNames[0],  specialButtons[0], null);
        builder.addBooleanProperty("Special Buttons/" + specialButtonNames[1],  specialButtons[1], null);

        builder.addBooleanProperty("Axis/Stick Pressed/Left",  stickPressed[0], null);
        builder.addBooleanProperty("Axis/Stick Pressed/Right",  stickPressed[1], null);

        builder.addBooleanProperty("POVs/Down",  povs[0], null);
        builder.addBooleanProperty("POVs/Left",  povs[1], null);
        builder.addBooleanProperty("POVs/Right",  povs[2], null);
        builder.addBooleanProperty("POVs/Up",  povs[3], null);

    }

    /**
     * Creates a new {@code NTJoystick} instance that publishes joystick data through Sendables
     *
     * @param controller The Xbox controller to get the joystick data from.
     */
    public static NTJoystick from(CommandXboxController controller){
        return new NTJoystick(controller);
    }

    /**
     * Creates a new {@code NTJoystick} instance that publishes joystick data through Sendables
     *
     * @param controller The PS5 controller to get the joystick data from.
     */
    public static NTJoystick from(CommandPS5Controller controller){
        return new NTJoystick(controller);
    }

    /**
     * Creates a new {@code NTJoystick} instance that publishes joystick data through Sendables
     *
     * @param controller The PS4 controller to get the joystick data from.
     */
    public static NTJoystick from(CommandPS4Controller controller){
        return new NTJoystick(controller);
    }

    /**
     * Creates a new {@code NTJoystick} instance that publishes joystick data through Sendables
     *
     * @param double[] rightAxis The right joystick axis values.
     * @param double[] leftAxis The left joystick axis values.
     * @param double[] triggers The trigger axis values.
     * @param boolean[] buttons The button states.
     * @param boolean[] bumpers The bumper states.
     * @param boolean[] specialButtons The special button states.
     * @param boolean[] stickPressed The stick pressed states.
     * @param boolean[] povs The POV hat states.
     * @param String[] buttonNames The names of the buttons.
     * @param String[] specialButtonNames The names of the special buttons.
     * 
     */
    public static NTJoystick from(
        DoubleSupplier[] rightAxis,
        DoubleSupplier[] leftAxis,
        DoubleSupplier[] triggers,
        BooleanSupplier[] buttons,
        BooleanSupplier[] bumpers,
        BooleanSupplier[] specialButtons,
        BooleanSupplier[] stickPressed,
        BooleanSupplier[] povs,
        String[] buttonNames,
        String[] specialButtonNames){
        return new NTJoystick(
            rightAxis, leftAxis,
            triggers, buttons,
            bumpers, specialButtons,
            stickPressed, povs,
            buttonNames, specialButtonNames);
    }

}
