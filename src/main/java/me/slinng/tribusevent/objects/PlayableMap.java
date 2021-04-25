package me.slinng.tribusevent.objects;

import me.slinng.tribusevent.Core;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class PlayableMap implements Cloneable, ConfigurationSerializable {

    private final String mapName, eventName;
    private final List<Location> spawnLocations;
    private Location fallbackLocation, lobbyLocation;

    public PlayableMap(String mapName, String eventName, Location fallbackLocation) {
        this.mapName = mapName;
        this.eventName = eventName;
        this.fallbackLocation = fallbackLocation;
        spawnLocations = new ArrayList<>();

    }
    public PlayableMap(String mapName, String eventName, Location fallbackLocation, Location lobbyLocation, List<Location> pinpoints) {
        this.mapName = mapName;
        this.eventName = eventName;
        this.lobbyLocation = lobbyLocation;
        this.fallbackLocation = fallbackLocation;
        this.spawnLocations = pinpoints;

    }

    public String getMapName() {
        return mapName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setLobbyLocation(Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public void addSpawnLocation(Location location) {
        this.spawnLocations.add(location);
    }

    public void setFallbackLocation(Location fallbackLocation) {
        this.fallbackLocation = fallbackLocation;
    }


    public Location getFallbackLocation() {
        return fallbackLocation;
    }

    public void removeSpawnLocation(Location location) {
        this.spawnLocations.remove(location);
    }

    public List<Location> getSpawnLocations()
    {
        return spawnLocations;
    }




    public static PlayableMap deserialize(Map<String, Object> deserializable) {
        String name = (String) deserializable.get("name");
        String event = (String) deserializable.get("event");
        Location lobby = (Location) deserializable.get("lobby");
        Location fallback = (Location) deserializable.get("fallback");
        List<Location> pinpoints = Core.cast(deserializable.get("pinpoints"));

        return new PlayableMap(name, event, fallback, lobby, pinpoints);
    }
    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> serialized = new HashMap<>();

        serialized.put("name", mapName);
        serialized.put("event", eventName);
        serialized.put("fallback", fallbackLocation);
        serialized.put("lobby", lobbyLocation);
        serialized.put("pinpoints", spawnLocations);

        return serialized;
    }


}
