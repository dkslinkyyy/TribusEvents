package me.slinng.tribusevent.commands.sub;

import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.objects.storage.MapStorage;
import me.slinng.tribusevent.objects.PlayableMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetFallbackLocationCommand extends SubCommand{


    public SetFallbackLocationCommand() {
        super("events", "setfallback", "tribus.admin", "<event>");
    }

    @Override
    public void executeSub(CommandSender s, String[] args) {


        Player p = (Player) s;
        if(args.length > 0 && args[0].equalsIgnoreCase("LMS")) {

            PlayableMap playableMap = null;
            if(MapStorage.exists("LMS")) {
                playableMap = MapStorage.fetch("LMS");

            }else{
                playableMap = new PlayableMap("LMS", "LMS", p.getLocation());
            }
            playableMap.setFallbackLocation(p.getLocation());
            MapStorage.save(playableMap);
            Core.i.getTextUtil().sendTitleMessage("&eSatte fallback location för LMS", 5,15,5, p);
        }



    }
}
