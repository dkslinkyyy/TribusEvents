package me.slinng.tribusevent.event.ePlayer;

import me.slinng.tribusevent.event.Event;
import org.bukkit.entity.Player;

public class EPlayer{

    private final Player player;
    private boolean isDead = false;



    public EPlayer(Player player) {
        this.player = player;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public Player getBukkitPlayer() {
        return player;
    }






}
