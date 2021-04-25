package me.slinng.tribusevent.commands.sub;

import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.objects.storage.MapStorage;
import me.slinng.tribusevent.objects.PlayableMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetEventLobbyLocationCommand extends SubCommand{


    public SetEventLobbyLocationCommand() {
        super("events", "setlobby", "tribusevents.admin", "<event>");
    }

    @Override
    public void executeSub(CommandSender s, String[] args) {

        Player p = (Player) s;

        PlayableMap map = null;

        if(args.length > 0 && args[0].equalsIgnoreCase("LMS")) {
            if(!MapStorage.exists("LMS")) {
                p.sendMessage(Core.i.trans("&cDu måste sätta ett ställe för fallback! /events setfallback LMS"));
                return;
            }
            map = MapStorage.fetch("LMS");
            System.out.println(map.getMapName());
            map.setLobbyLocation(p.getLocation());
            MapStorage.save(map);
            Core.i.getTextUtil().sendTitleMessage("&eDu satte lobby för LMS",5,5,5, p);
        }
    }
}
