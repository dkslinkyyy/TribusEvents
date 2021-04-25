package me.slinng.tribusevent.gui;

import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.miscelleanous.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class GUI implements Listener {

    private String name;
    private int size,slot = 0;

    private List<GUIItem> guiItems;

    private int[] endRows = {};


    private Inventory inventory;

    public GUI(String name, int size) {
        this.name = name;
        this.size = size;
        this.guiItems = new ArrayList<>();
        this.inventory = Bukkit.createInventory(null, size, Core.i.trans(name));

        Bukkit.getServer().getPluginManager().registerEvents(this, Core.i);
    }

    protected abstract GUIItem[] guiDefaultItems();


    protected abstract void onPlayerOpenGUI();

    GUIItem guiItem;

    public GUIItem getItem(int slot) {

        guiItems.stream().forEach(gItem ->
       {

           if(gItem.getSlot() == slot) {
              this.guiItem = gItem;
           }
       });
       return guiItem;
    }


    protected abstract boolean refreshItems();



    private void refresh() {
        List<GUIItem> temp = new ArrayList<>(guiItems);

        clear();

        temp.forEach(self -> setItem(self, self.getSlot()));
        temp.clear();
    }

    @EventHandler
    public void event(InventoryClickEvent event) {

        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || event.getCurrentItem().getItemMeta() == null) {
            return;
        }

        Player p = (Player) event.getWhoClicked();

        String clickedItemName = event.getCurrentItem().getItemMeta().getDisplayName();

        if(event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
            event.setCancelled(true);
            return;
        }

        GUIItem guiItem = getClickedItem(clickedItemName);

        if(guiItem == null) return;
        guiItem.getButton().onClick(event.getClick(), event.getSlot(), guiItem, (Player) event.getWhoClicked());

        event.setCancelled(true);
    }

    protected void addBackItem(GUI gui, Player p, int slot) {
        setItem(new GUIItem("back_item", new ItemCreator(Material.WOOD_DOOR))
                .title("&8<- &c&lTILLBAKA TILL EVENTS")
                .lore(new String[]{"", "&7Gå tillbaka till huvudmenyn"})
                .button(new Button() {
                    @Override
                    public void onClick(ClickType ct, int slot, GUIItem guiItem, Player whoClicked) {
                        gui.open(p);
                    }
                }),slot);
    }

    private GUIItem getClickedItem(String name) {
        return guiItems.stream().filter(gItem -> name.equals(Core.i.trans(gItem.getTitle()))).findFirst().orElse(null);
    }

    public abstract void clickableButton(Player p, int slot);




    public GUI clear() {
        this.inventory.clear();
        guiItems.clear();
        return this;
    }

    public void removeItem(GUIItem guiItem, int slot) {
        guiItems.remove(guiItem);
        this.inventory.setItem(slot, new ItemStack(Material.AIR));
    }

    public void setItem(GUIItem guiItem, int slot) {
        guiItems.add(guiItem.slot(slot));
        if(guiItem.getData() != 0) {
            this.inventory.setItem(slot, guiItem.getItemWithData());

        }else{
            this.inventory.setItem(slot, guiItem.getItem());

        }
    }

    public GUI addItem(GUIItem guiItem) {
        guiItem.slot(slot);
        guiItems.add(guiItem);
        if(guiItem.getData() != 0) {
            this.inventory.setItem(this.slot, guiItem.getItemWithData());

        }else{
            this.inventory.setItem(this.slot, guiItem.getItem());

        }
        this.slot = slot+1;
        return this;

    }

    public GUI filler(boolean filler) {
        if(filler) {
            int slot = 0;
            while(slot != this.inventory.getSize()) {
                this.inventory.setItem(slot, new GUIItem("nothing_here", new ItemCreator(Material.STAINED_GLASS_PANE)).title("").data(7).getItemWithData());
                slot++;
            }
        }
        return this;
    }


    public GUI beginAtSlot(int slot) {
        this.slot = slot;
        return this;
    }


    protected List<GUIItem> getGuiItems() {
        return guiItems;
    }

    public void open(Player p) {
        if(refreshItems()) {
            refresh();
        }

        onPlayerOpenGUI();
        p.openInventory(inventory);
    }

    public String getName() {
        return name;
    }

    public void close(Player p) {
        p.closeInventory();
    }

    public int getSize() {
        return size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
