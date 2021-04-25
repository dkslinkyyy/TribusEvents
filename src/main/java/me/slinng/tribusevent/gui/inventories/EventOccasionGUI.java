package me.slinng.tribusevent.gui.inventories;

import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.gui.Button;
import me.slinng.tribusevent.gui.GUI;
import me.slinng.tribusevent.gui.GUIItem;
import me.slinng.tribusevent.miscelleanous.ItemCreator;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class EventOccasionGUI extends GUI {
    
    private String time;
    private final String name;

    private String[] allWeekDays = new String[]{"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
    private final HashMap<String, Boolean> activeWeekdays = new HashMap<>();

    public EventOccasionGUI(String name) {
        super(name, 9);
        this.name = name;
    }

    private boolean hasWeekday(List<String> weekdays, String w) {
        return weekdays.contains(w);
    }

    private void updateWeekdays(List<String> weekdays) {
        Core.i.getConfigManager().getConfigFile().getCustomConfig().set("config.EVENTS.LMS.occasion.weekdays", weekdays);
        Core.i.getConfigManager().getConfigFile().saveConfig();
        Core.i.getConfigManager().getConfigFile().updateConfig();
        Core.i.getConfigManager().loadAllConfigs();
    }




    @Override
    protected GUIItem[] guiDefaultItems() {
        return new GUIItem[0];
    }

    @Override
    protected void onPlayerOpenGUI() {

        List<String> weekdays = Core.i.getConfigManager().getConfigFile().getCustomConfig().getStringList("config.EVENTS."+ name +".occasion.weekdays");
        weekdays.forEach(System.out::println);

        time = Core.i.getConfig().getString("config.EVENTS." + name + ".occasion.time");


        System.out.println(weekdays.get(0));
        int count = 1;

        for(String w : allWeekDays) {
            System.out.println(hasWeekday(weekdays, w));
            setItem(new GUIItem(w, new ItemCreator(Material.STAINED_GLASS))
                    .title(hasWeekday(weekdays, w) ? "&a" + w : "&c" + w)
                    .data(hasWeekday(weekdays, w) ? 5 : 14)
                    .button(new Button() {
                        @Override
                        public void onClick(ClickType ct, int slot, GUIItem guiItem, Player whoClicked) {
                            removeItem(guiItem, slot);
                            setItem(guiItem.title(hasWeekday(weekdays, w) ? "&c" + w : "&a" + w).data(hasWeekday(weekdays, w) ? 14 : 5), slot);
                            if(hasWeekday(weekdays, w)) {
                                weekdays.remove(w);
                            }else{
                                weekdays.add(w);
                            }

                        }
                    }), count);

            count++;
        }




        setItem(new GUIItem("save", new ItemCreator(Material.MAP))
                .title("&a&lSPARA")
                .lore(new String[]{
                        "",
                        "&7Klicka för att spara"
                })
                .button(new Button() {
                    @Override
                    public void onClick(ClickType ct, int slot, GUIItem guiItem, Player whoClicked) {
                        updateWeekdays(weekdays);
                        whoClicked.getOpenInventory().close();

                    }
                }),8);




    }

    @Override
    protected boolean refreshItems() {
        return true;
    }



    @Override
    public void clickableButton(Player p, int slot) {

    }
}
