package me.slinng.tribusevent.commands.sub;

import me.slinng.tribusevent.gui.inventories.EventsGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConfigureEventCommand extends SubCommand {

    public ConfigureEventCommand() {
        super("events", "configure", "tribusevents.admin", "<event>");
    }

    @Override
    public void executeSub(CommandSender s, String[] args) {
        Player p = (Player) s;


        if(args.length > 0) {
            new EventsGUI(p).open(p);

        }
    }
}
