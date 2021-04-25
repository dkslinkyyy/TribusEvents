package me.slinng.tribusevent.gui.inventories;

import me.slinng.tribusevent.gui.Button;
import me.slinng.tribusevent.gui.GUI;
import me.slinng.tribusevent.gui.GUIItem;
import me.slinng.tribusevent.miscelleanous.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class EventRewardsGUI extends GUI {

    public EventRewardsGUI(String name) {

        super(name, 9 * 4);

        filler(true);

        setItem(new GUIItem("addReward", new ItemCreator(Material.EMERALD))
                .title("&a&lLÄGG TILL BELÖNING")
                .lore(new String[]{"",
                        "&7Klicka för att lägga till en belöning"})
                .button(new Button() {
                    @Override
                    public void onClick(ClickType ct, int slot, GUIItem guiItem, Player whoClicked) {

                    }
                }), 1);
    }

    @Override
    protected GUIItem[] guiDefaultItems() {
        return new GUIItem[0];
    }

    @Override
    protected void onPlayerOpenGUI() {

    }

    @Override
    protected boolean refreshItems() {
        return false;
    }

    @Override
    public void clickableButton(Player p, int slot) {

    }

}
