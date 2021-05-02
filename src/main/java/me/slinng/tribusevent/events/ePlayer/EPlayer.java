package me.slinng.tribusevent.events.ePlayer;

import jdk.nashorn.internal.runtime.PropertyMap;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class EPlayer {

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



    public void disguise() {

    }

    public void unDisguise() {

    }

}
