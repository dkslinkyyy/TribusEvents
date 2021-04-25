package me.slinng.tribusevent.miscelleanous;

import me.slinng.tribusevent.Core;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.stream.Stream;

public class ItemCreator {

    private final Material material;
    private String name;
    private String[] lore;

    private ItemStack emptyItem = new ItemStack(Material.STAINED_GLASS_PANE, 1,  (byte) 7);


    public ItemCreator(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemStack getOutcome(Material material, String name, String[] desc, int amount) {
        if(material == null || name == null || desc == null || amount == 0)
            return emptyItem;

        ItemStack i = new ItemStack(material, amount);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(Core.i.trans(name));
        Stream<String> translate = Arrays.stream(desc).map(l -> l = Core.i.trans(l));
        String[] test = translate.toArray(String[]::new);
        im.setLore(Arrays.asList(test));
        i.setItemMeta(im);

        return i;
    }

    public ItemStack getOutcome(Material material, String name, String[] desc, int amount, int data) {



        ItemStack i = new ItemStack(material, amount, (byte) data);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(Core.i.trans(name));

        if(desc != null) {
            Stream<String> translate = Arrays.stream(desc).map(l -> l = Core.i.trans(l));
            String[] test = translate.toArray(String[]::new);
            im.setLore(Arrays.asList(test));
        }

        i.setItemMeta(im);

        return i;
    }

}
