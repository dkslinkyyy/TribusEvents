package me.slinng.tribusevent.gui;


import me.slinng.tribusevent.miscelleanous.ItemCreator;
import org.bukkit.inventory.ItemStack;

public class GUIItem {


    private String title;
    private String name;
    private String[] lore;
    private int slot, id;
    private ItemCreator itemCreator;
    private boolean b = false;
    private int data;

    public GUIItem(String name, ItemCreator itemCreator) {
        this.name = name;
        this.itemCreator = itemCreator;
    }



    public GUIItem title(String title) {
        this.title = title;
        return this;
    }

    public GUIItem lore(String[] lore) {
        this.lore = lore;
        return this;
    }

    public GUIItem slot(int slot) {
        this.slot = slot;
        return this;
    }

    public GUIItem beginAtSlot(boolean b) {
        this.b = b;
        return this;
    }


    public GUIItem data(int data) {
        this.data = data;
        return this;
    }

    public int getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String[] getLore() {
        return lore;
    }

    public int getSlot() {
        if(b) {
            this.slot += slot;
        }
        return slot;
    }

    public GUIItem id(int id){
        this.id = id;
        return this;
    }


    private Button button;

    public GUIItem button(Button button) {
        this.button = button;
        return this;
    }
    public Button getButton() {
        return button;
    }

    public int getID() {
        return id;
    }
    public ItemStack getItem() {
        return itemCreator.getOutcome(itemCreator.getMaterial(), title, lore, 1);
    }

    public ItemStack getItemWithData() {
        return itemCreator.getOutcome(itemCreator.getMaterial(), title, lore, 1, data);
    }
}
