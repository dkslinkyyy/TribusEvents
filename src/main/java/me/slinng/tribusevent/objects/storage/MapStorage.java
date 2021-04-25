package me.slinng.tribusevent.objects.storage;

import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.objects.PlayableMap;

public class MapStorage {



    public static boolean exists(String mapName) {
        return Core.i.getConfigManager().getMapsConfig().getCustomConfig().get("MAPS." + mapName) != null;
    }

    public static PlayableMap fetch(String mapName) {

        return (PlayableMap) Core.i.getConfigManager().getMapsConfig().getCustomConfig().get("MAPS." + mapName);
    }

    public static void save(PlayableMap map) {
        Core.i.getConfigManager().getMapsConfig().getCustomConfig().set("MAPS." + map.getMapName(), map);
        Core.i.getConfigManager().getMapsConfig().saveConfig();
    }
}
