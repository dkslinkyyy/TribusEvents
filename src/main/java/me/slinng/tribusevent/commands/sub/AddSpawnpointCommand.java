package me.slinng.tribusevent.commands.sub;

import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.objects.storage.MapStorage;
import me.slinng.tribusevent.objects.PlayableMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddSpawnpointCommand extends SubCommand {


    public AddSpawnpointCommand() {
        super("events", "addpoint", "tribusevents.admin", "<event>");
    }

    @Override
    public void executeSub(CommandSender s, String[] args) {

        Player p = (Player) s;

        PlayableMap map = null;

        if(args.length > 0 && args[0].equalsIgnoreCase("LMS")) {
           if(!MapStorage.exists("LMS")) {
               p.sendMessage(Core.i.trans("&6&lTribusMC &8» &cDu måste sätta en fallback-position /events setfallback lms"));
               return;
           }
           map = MapStorage.fetch("LMS");
           System.out.println(map.getMapName());
           map.addSpawnLocation(p.getLocation());
           MapStorage.save(map);
           Core.i.getTextUtil().sendTitleMessage("&eDu la till spawnpoint för LMS",5,15,5, p);
        }
    }
}
