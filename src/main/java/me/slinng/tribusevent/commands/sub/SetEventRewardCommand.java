package me.slinng.tribusevent.commands.sub;

import me.slinng.tribusevent.commands.CommandManager;
import org.bukkit.command.CommandSender;

public class SetEventRewardCommand extends SubCommand {


    public SetEventRewardCommand() {
        super("events", "setreward", "tribusevents.admin", "<event>");
    }

    @Override
    public void executeSub(CommandSender s, String[] args) {

    }
}
