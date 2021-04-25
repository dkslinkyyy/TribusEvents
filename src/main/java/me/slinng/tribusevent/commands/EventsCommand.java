package me.slinng.tribusevent.commands;

import me.slinng.tribusevent.commands.sub.*;
import me.slinng.tribusevent.gui.inventories.EventsGUI;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventsCommand extends CommandManager{
    public EventsCommand() {
        super("events", "", false);

        registerSubCommand(new ConfigureEventCommand());
        registerSubCommand(new AddSpawnpointCommand());
        registerSubCommand(new JoinEventCommand());
        registerSubCommand(new EventForceStartCommand());
        registerSubCommand(new SetEventLobbyLocationCommand());
        registerSubCommand(new SetFallbackLocationCommand());
        registerSubCommand(new SetEventRewardCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        new EventsGUI((Player) sender).open((Player) sender);

    }
}
