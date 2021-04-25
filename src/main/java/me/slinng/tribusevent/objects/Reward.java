package me.slinng.tribusevent.objects;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Reward implements Cloneable, ConfigurationSerializable {


    private ItemStack i;

    public Reward(ItemStack paramItem) {
        this.i = paramItem;
    }



    public void give(Player player) {

    }


    @Override
    public Map<String, Object> serialize() {
        return null;
    }
}
