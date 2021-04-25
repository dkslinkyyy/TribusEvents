package me.slinng.tribusevent.Listeners;

import me.slinng.tribusevent.events.EventState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;

public class InventoryClickListener implements Listener {


    @EventHandler
    public void onClickEvent(InventoryClickEvent e) {




    }



    private EventState fetchByName(String name) {
        return Arrays.stream(EventState.values()).filter(es -> es.name().equals(name)).findFirst().orElse(null);
    }
}
