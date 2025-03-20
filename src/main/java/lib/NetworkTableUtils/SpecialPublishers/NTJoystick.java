package lib.NetworkTableUtils.SpecialPublishers;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import lib.NetworkTableUtils.NormalPublishers.NTArrayBoolean;
import lib.NetworkTableUtils.NormalPublishers.NTArrayDouble;

public class NTJoystick {

    private NTArrayDouble rightAxis;
    private NTArrayDouble leftAxis;
    private NTArrayDouble triggers;
    private NTArrayBoolean buttons;
    private NTArrayBoolean bumpers;
    private NTArrayBoolean specialButtons;
    private NTArrayBoolean stickPressed;
    private NTArrayBoolean povs;
    
    public NTJoystick(String key){
        this.rightAxis = new NTArrayDouble(key + "/rightAxis");
        this.leftAxis = new NTArrayDouble(key + "leftAxis");
        this.triggers = new NTArrayDouble(key + "/triggers");
        this.buttons = new NTArrayBoolean(key + "/buttons");
        this.bumpers = new NTArrayBoolean(key + "/bumpers");
        this.specialButtons = new NTArrayBoolean(key + "/view-menu");
        this.stickPressed = new NTArrayBoolean(key+ "/sticksPressed");
        this.povs = new NTArrayBoolean(key + "/povs");

    }

    public void sendXboxController(CommandXboxController xbox){
        rightAxis.sendDouble(new double[]{xbox.getRightY(), xbox.getRightX()});
        leftAxis.sendDouble(new double[]{xbox.getLeftY(), xbox.getLeftX()});
        triggers.sendDouble(new double[]{xbox.getLeftTriggerAxis(), xbox.getRightTriggerAxis()});
        buttons.sendBoolean(new boolean[]{xbox.a().getAsBoolean(), xbox.b().getAsBoolean(), xbox.x().getAsBoolean(), xbox.y().getAsBoolean()});
        bumpers.sendBoolean(new boolean[]{xbox.leftBumper().getAsBoolean(), xbox.rightBumper().getAsBoolean()});
        specialButtons.sendBoolean(new boolean[]{xbox.start().getAsBoolean(), xbox.back().getAsBoolean()});
        stickPressed.sendBoolean(new boolean[]{xbox.leftStick().getAsBoolean(), xbox.rightStick().getAsBoolean()});
        povs.sendBoolean(new boolean[]{
            xbox.povDown().getAsBoolean(),
            xbox.povLeft().getAsBoolean(),
            xbox.povRight().getAsBoolean(),
            xbox.povUp().getAsBoolean()});

    }


}
