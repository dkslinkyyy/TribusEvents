package me.slinng.tribusevent.commands.sub;

import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.commands.CommandManager;
import org.bukkit.command.CommandSender;

public class EventForceStartCommand extends SubCommand {


    public EventForceStartCommand() {
        super("events", "forcestart","tribusevent.admin", "<event>");
    }



    @Override
    public void executeSub(CommandSender s, String[] args) {
        if(args.length > 0 && args[0].equalsIgnoreCase("LMS")) {
            try {
                Core.i.getEventController().getLMS().beginSearchForPlayers();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
