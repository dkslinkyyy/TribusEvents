package me.slinng.tribusevent.listeners;

import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.events.EventState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;

public class PlayerQuitListener implements Listener {


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Core.i.getEventController().getLMS().unDisguise(e.getPlayer());



    }




}
