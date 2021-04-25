package me.slinng.tribusevent.gui.inventories;

import me.slinng.tribusevent.gui.Button;
import me.slinng.tribusevent.gui.GUI;
import me.slinng.tribusevent.gui.GUIItem;
import me.slinng.tribusevent.miscelleanous.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ConfigureEventGUI extends GUI {


    public ConfigureEventGUI(String name, Player p) {
        super(name, 9*4);

        filler(true);
        addBackItem(new EventsGUI(p), p, 27);

        setItem(new GUIItem("time", new ItemCreator(Material.WATCH))
                .title("&E&LAVSATT TID")
                .lore(new String[]{
                        "",
                        "&7Klicka för att ändra tiden"
                }).button(new Button() {
                    @Override
                    public void onClick(ClickType ct, int slot, GUIItem guiItem, Player whoClicked) {
                        new EventOccasionGUI(name).open(p);
                    }
                }),11);


        if(name.equals("LMS")) {
            setItem(new GUIItem("kit", new ItemCreator(Material.IRON_SWORD))
                    .title("&9&lKIT")
                    .lore(new String[]{
                            "",
                            "&7Klicka för att ändra kittet för LMS"
                    }).button(new Button() {
                        @Override
                        public void onClick(ClickType ct, int slot, GUIItem guiItem, Player whoClicked) {

                        }
                    }),15);


            setItem(new GUIItem("kit", new ItemCreator(Material.DIAMOND))
                    .title("&c&lPRIS")
                    .lore(new String[]{
                            "",
                            "&7Klicka för att lägga till ett pris"
                    }).button(new Button() {
                        @Override
                        public void onClick(ClickType ct, int slot, GUIItem guiItem, Player whoClicked) {

                        }
                    }),22);
        }

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
