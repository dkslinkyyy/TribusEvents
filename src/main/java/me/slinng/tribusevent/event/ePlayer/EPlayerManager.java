package me.slinng.tribusevent.event.ePlayer;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EPlayerManager {

    private final List<EPlayer> eventPlayers;

    public EPlayerManager() {
        eventPlayers= new ArrayList<>();
    }


    public List<EPlayer> getEventPlayers() {
        return eventPlayers;
    }


    public int getEventTotalPlayers() {
        return eventPlayers.size();
    }

    public void add(EPlayer ePlayer) {
        eventPlayers.add(ePlayer);
    }

    public void remove(EPlayer ePlayer) {
        eventPlayers.remove(ePlayer);
    }


    public EPlayer fetchByPlayer(Player player) {
        return eventPlayers.stream().filter(ePlayer ->  ePlayer.getBukkitPlayer() == player).findFirst().orElse(null);
    }

    public boolean contains(Player player) {
        EPlayer ep = eventPlayers.stream().filter(ePlayer ->  ePlayer.getBukkitPlayer() == player).findFirst().orElse(null);
        return ep != null;
    }
}
