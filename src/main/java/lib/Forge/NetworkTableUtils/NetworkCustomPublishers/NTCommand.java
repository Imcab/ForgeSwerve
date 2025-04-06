package lib.Forge.NetworkTableUtils.NetworkCustomPublishers;

import edu.wpi.first.wpilibj2.command.Command;
import lib.Forge.NetworkTableUtils.NetworkMultipleData.NTPublisher;

public class NTCommand extends Command{

    protected NTCommand(String table, String name){
        NTPublisher.publish(table, name, this);
    }

}
