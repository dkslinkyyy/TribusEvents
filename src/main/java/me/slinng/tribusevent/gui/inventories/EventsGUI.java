package me.slinng.tribusevent.gui.inventories;

import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.gui.Button;
import me.slinng.tribusevent.gui.GUI;
import me.slinng.tribusevent.gui.GUIItem;
import me.slinng.tribusevent.gui.Placeholder;
import me.slinng.tribusevent.miscelleanous.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class EventsGUI extends GUI {

    private String itemTitle;
    private String itemLore;
    private int slot;

    private Player p;

    public EventsGUI(Player p) {
        super("&6&lEVENTS", 9*3);

        this.p = p;


        filler(true);

        addEventItem("LMS", p);
        addEventItem("SERVER_RESTART", p);



    }

    @Override
    protected GUIItem[] guiDefaultItems() {
        return new GUIItem[0];
    }

    @Override
    protected void onPlayerOpenGUI() {


    }

    @Override
    public boolean refreshItems() {
        return true;
    }

    @Override
    public void clickableButton(Player p, int slot) {

    }


    private void addEventItem(String event, Player p) {



        String isStaff = p.hasPermission("tribusevents.admin") ? "is_staff" : "not_staff";

        String title = Core.i.getConfigManager().getConfigFile().getCustomConfig().getString("config.EVENTS." + event + ".ITEM_FORMAT." + isStaff + ".title");
        Material m = Material.valueOf(Core.i.getConfigManager().getConfigFile().getCustomConfig().getString("config.EVENTS." + event + ".ITEM_FORMAT." + isStaff + ".material"));
        int slot = Core.i.getConfigManager().getConfigFile().getCustomConfig().getInt("config.EVENTS." + event + ".ITEM_FORMAT." + isStaff + ".slot");
        List<String> tempLore = Core.i.getConfigManager().getConfigFile().getCustomConfig().getStringList("config.EVENTS." + event + ".ITEM_FORMAT." + isStaff + ".lore");


        String[] toArrayLore = convertedEventLore("LMS", tempLore);


        setItem(new GUIItem(event, new ItemCreator(m))
                .title(title)
                .lore(toArrayLore)
                .button(new Button() {
                    @Override
                    public void onClick(ClickType clickType, int slot, GUIItem guiItem, Player whoClicked) {

                        if(clickType == ClickType.RIGHT) {
                            if(p.hasPermission("tribusevents.admin")) {
                                new ConfigureEventGUI(event, p).open(p);
                            }

                        }
                    }
                }), slot);

    }


    private String[] convertedEventLore(String event, List<String> lore) {
        String converted[] = new String[lore.size()];

        for(int i = 0; i < lore.size(); i++) {
            for(Placeholder placeholder : getEventPlaceholders(event)) {
                lore.set(i, lore.get(i).replaceAll(placeholder.getPlaceholder(), placeholder.getReplaced()));
                converted[i] = lore.get(i);
            }
        }

        return converted;
    }

    private Placeholder[] getEventPlaceholders(String event) {
        return new Placeholder[] {
                new Placeholder("%kit%", event.equals("LMS") ? Core.i.getEventController().getLMS().getEventName() : ""),
                new Placeholder("%price%", event.equals("LMS") ? Core.i.getEventController().getLMS().getEventName() : ""),
                new Placeholder("%weekdays%", Core.i.getConfigManager().getConfigFile().getCustomConfig().getStringList("config.EVENTS." + event + ".occasion.weekdays").toString()),
                new Placeholder("%tod%", Core.i.getConfigManager().getConfigFile().getCustomConfig().getString("config.EVENTS." + event + ".occasion.time"))

        };
    }
}
