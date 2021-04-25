package me.slinng.tribusevent.events;

import com.cryptomorin.xseries.XMaterial;
import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.events.ePlayer.EPlayer;
import me.slinng.tribusevent.objects.PlayableMap;
import me.slinng.tribusevent.miscelleanous.timer.CheckTimer;
import me.slinng.tribusevent.miscelleanous.timer.RunnableCode;
import me.slinng.tribusevent.miscelleanous.timer.TimerType;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LMS extends Event {

    private PlayableMap map;
    private HashMap<Player, ArrayList<CachedItem>> cachedItems;
    private int alive = 0;

    public LMS() {
        super("LMS", 0, 36, EventState.UNREACHABLE, new EventOccasion("18:00"));

        setCheck(false);

        cachedItems = new HashMap<>();

    }


    private void equip(Player player) {
        player.setHealth(20.0);
        player.setFoodLevel(20);

        PlayerInventory playerINV = player.getInventory();

        playerINV.clear();

        playerINV.setHelmet(XMaterial.IRON_HELMET.parseItem());
        playerINV.setChestplate(XMaterial.IRON_CHESTPLATE.parseItem());
        playerINV.setLeggings(XMaterial.IRON_LEGGINGS.parseItem());
        playerINV.setBoots(XMaterial.IRON_BOOTS.parseItem());


        ItemStack splash = XMaterial.POTION.parseItem();
        assert splash != null;
        splash.setDurability((short) 16421);

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            playerINV.setItem(i, splash);
        }
        playerINV.setItem(0, XMaterial.IRON_SWORD.parseItem());

    }


    private static class CachedItem {

        private ItemStack item;
        private int slot;

        CachedItem(ItemStack paramItem, int paramSlot) {
            this.item = paramItem;
            this.slot = paramSlot;
        }

    }


    private void unequip(EPlayer ePlayer) {
        ePlayer.getBukkitPlayer().getInventory().clear();
        clear(ePlayer.getBukkitPlayer());

        ArrayList<CachedItem> playerCashedItems = cachedItems.get(ePlayer.getBukkitPlayer());

        if (playerCashedItems == null) return;

        playerCashedItems.forEach(cached -> {

            String typeName = cached.item.getType().name();
            if (typeName.endsWith("_HELMET")) ePlayer.getBukkitPlayer().getInventory().setHelmet(cached.item);
            if (typeName.endsWith("_CHESTPLATE")) ePlayer.getBukkitPlayer().getInventory().setChestplate(cached.item);
            if (typeName.endsWith("_LEGGINGS")) ePlayer.getBukkitPlayer().getInventory().setLeggings(cached.item);
            if (typeName.endsWith("_BOOTS")) ePlayer.getBukkitPlayer().getInventory().setBoots(cached.item);
            else ePlayer.getBukkitPlayer().getInventory().setItem(cached.slot, cached.item);
        });

    }

    private boolean isEquipable(ItemStack i) {
        ItemStack test = new ItemStack(i.getType());

        try {
            test.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }

    private void clear(Player p) {
        cachedItems.put(p, new ArrayList<>());
        for (int i = 0; i < p.getInventory().getSize(); i++) {
            if (p.getInventory().getItem(i) == null) continue;

            cachedItems.get(p).add(new CachedItem(p.getInventory().getItem(i), i));
        }

        for (ItemStack armor : p.getInventory().getArmorContents()) {
            cachedItems.get(p).add(new CachedItem(armor, 0));

        }

        p.getInventory().clear();

        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
    }

    @Override
    public void onJoin(Player p) {
        clear(p);
    }

    @Override
    protected void onStart(List<EPlayer> ePlayers) {
        alive = ePlayers.size();
        ePlayers.forEach(ePlayer -> equip(ePlayer.getBukkitPlayer()));
    }

    @Override
    public void onFinish(EPlayer ep) {
        unequip(ep);
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent e, Player p, Player killer, List<EPlayer> ePlayers) {
        alive -= 1;

        e.getDrops().clear();

        if (alive == 1) {
            p.spigot().respawn();

            ePlayers.forEach(ePlayer -> ePlayer.getBukkitPlayer().teleport(playableMap.getFallbackLocation()));


            CheckTimer ct = new CheckTimer(TimerType.CHECK, 1);

            ct.execute(() -> {
                ePlayers.forEach(this::onFinish);
                Core.i.getTextUtil().sendToAll("&6&lTribusMC &8» &a&l" + killer.getName() + " vann " + eventName + " eventet!");
            });


        } else {
            p.spigot().respawn();
            p.setGameMode(GameMode.ADVENTURE);
            p.setAllowFlight(true);
            p.setFlying(true);


            /*
            GUIItem leave = new GUIItem("leave", new ItemCreator(Material.BED))
                    .title("&c&lLÄMNA")
                    .lore(new String[]{"", "&7Klicka för att lämna", ""})
                    .button(new Button() {
                        @Override
                        public void onClick(ClickType ct, int slot, GUIItem guiItem, Player whoClicked) {
                            dead.getPlayer().teleport(playableMap.getFallbackLocation());
                            eventPlayerManager.getEventPlayers().remove(dead);
                        }
                    });


             */


            Core.i.getTextUtil().sendTitleMessage("&c&lEliminerad!", 5, 25, 5, p);
        }


    }


    @Override
    protected void onPlayerDamageEvent(EntityDamageEvent e, Player p) {
        e.setCancelled(isState(EventState.STARTING));
    }

    @Override
    protected void onPlayerDamagePlayer(EntityDamageByEntityEvent e, Player p) {
        e.setCancelled(isState(EventState.STARTING));
    }
}
