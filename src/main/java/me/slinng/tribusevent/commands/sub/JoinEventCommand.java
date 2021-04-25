package me.slinng.tribusevent.commands.sub;

import me.slinng.tribusevent.Core;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinEventCommand extends SubCommand{
    public JoinEventCommand() {
        super("events", "join", "", "<event>");
    }

    @Override
    public void executeSub(CommandSender s, String[] args) {

        if(args.length > 0 && args[0].equalsIgnoreCase("LMS")) {
            Core.i.getEventController().getLMS().join((Player) s);
        }

    }
}
