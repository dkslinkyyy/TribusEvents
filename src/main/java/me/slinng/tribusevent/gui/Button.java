package me.slinng.tribusevent.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;


public interface Button {

    void onClick(ClickType ct, int slot, GUIItem guiItem, Player whoClicked);

}
