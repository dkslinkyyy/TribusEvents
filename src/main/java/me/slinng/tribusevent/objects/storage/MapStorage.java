package me.slinng.tribusevent.objects.storage;

import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.objects.PlayableMap;
import org.bukkit.Location;

import java.util.List;


public class MapStorage {



    public static boolean exists(String mapName) {
        return Core.i.getConfigManager().getMapsConfig().getCustomConfig().get("MAPS." + mapName) != null;
    }

    public static PlayableMap fetch(String mapName) {

        String name = Core.i.getConfigManager().getMapsConfig().getCustomConfig().getString("MAPS." + mapName + ".name");
        String events = Core.i.getConfigManager().getMapsConfig().getCustomConfig().getString("MAPS." + mapName + ".event");
        Location fallback =(Location) Core.i.getConfigManager().getMapsConfig().getCustomConfig().get("MAPS." + mapName + ".fallback");
        Location lobby = (Location) Core.i.getConfigManager().getMapsConfig().getCustomConfig().get("MAPS." + mapName + ".lobby");
        List<?> pinpoints =  Core.i.getConfigManager().getMapsConfig().getCustomConfig().getList("MAPS." + mapName + ".pinpoints");
        return new PlayableMap(name, events, fallback, lobby, (List<Location>) pinpoints);
    }

    public static void save(PlayableMap map) {
        Core.i.getConfigManager().getMapsConfig().getCustomConfig().set("MAPS." + map.getMapName(), map.serialize());
        Core.i.getConfigManager().getMapsConfig().saveConfig();
    }
}
