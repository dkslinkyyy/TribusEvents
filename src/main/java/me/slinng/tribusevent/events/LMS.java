package me.slinng.tribusevent.events;

import com.cryptomorin.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.events.ePlayer.EPlayer;
import me.slinng.tribusevent.objects.PlayableMap;
import me.slinng.tribusevent.miscelleanous.timer.CheckTimer;
import me.slinng.tribusevent.miscelleanous.timer.RunnableCode;
import me.slinng.tribusevent.miscelleanous.timer.TimerType;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.Main;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.util.*;

public class LMS extends Event {

    private PlayableMap map;
    private final HashMap<Player, ArrayList<CachedItem>> cachedItems;
    private final HashMap<Player, Property> cachedProperties;
    private Scoreboard scoreboard;
    private Team disguisedTeam;
    private int alive = 0;


    public LMS() {
        super("LMS", 0, 36, EventState.UNREACHABLE, new EventOccasion("18:00"));

        setCheck(false);

        cachedItems = new HashMap<>();
        cachedProperties = new HashMap<>();

        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        if(scoreboard.getTeam("disguised") == null) {
            disguisedTeam = scoreboard.registerNewTeam("disguised");
        }else{
            disguisedTeam = scoreboard.getTeam("disguised");
        }

        disguisedTeam.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OWN_TEAM);


    }


    public void disguise(Player p) {
        disguisedTeam.addPlayer(p);
        changeSkin(p);
        refresh(p);
    }

    public void unDisguise(Player p) {
        disguisedTeam.removePlayer(p);
        changeSkin(p);
        refresh(p);

    }


    public void changeSkin(Player p) {
        GameProfile gp = ((CraftPlayer)p).getProfile();

        if(cachedProperties.get(p) != null) {
            gp.getProperties().clear();
            gp.getProperties().put("textures", cachedProperties.get(p));
            return;
        }

        String texture = "ewogICJ0aW1lc3RhbXAiIDogMTYxOTk5NjU4NzI2OCwKICAicHJvZmlsZUlkIiA6ICJhNjEwOTM1MzMzMzU0MGZjYWYyMDc4OGNiMDE5NWI3MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJXRVNMQVYiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6L" +
                "y90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWY4YmMyYzI4MjU0ZDY2NDRjODkwYjMxZTRiZWExM2E5YTVhMjg1YTM1Y2M4MDhhNjFmYzBhNGE0NDhkOGVhNyIKICAgIH0KICB9Cn0=";

        String signature = "vC6qWfI/MU9s/B7fhsKbw3RsOMKNMknDoSuEBT6c8IFsaryzRuJa/gTazfwlp16PT3/RTj1meQaWcFiHLJHNeh6vatFpAkgYqWpsVgLDOMHFIMbZV7SbhKY7NvpyeEKAOTl2j3PstyYKRLDK2Mvltveb4JysI8LunbHiECZnVgZ0HWRkboqH2Oe6L2O7bz2B7Zhd0ulJHk3re5hXw4/JqncOa2Ap1h645KQiesM7yuWNOoSPksELvCU32RBcS5GYN+pydyarXjLUgkWthtxyUE6IJdetMa9bdJivHoadIYSb7jB08+xoT2i4BW8Bmjf6mAYBzssfq00AT+WnCgChJxL5g1pFAoK6pI8fIPjG5W8OkmiXOyE0mDMXZ8GDo+dhUa49TdZe41rUyL75Hook9ug8T46uk+KisaBo3fIbY6mfU9zrcXXVwpoSgBcyFMWLIcaDvRJp9RwEaaVHb3G+VkkXJPdSdCwoE2XfhKqvZVO54123Zr2nxENQh77+DchOQDGSnoTn5AKYcCWuYixFzIDjd" +
                "g2gwDcrHoWQmkdWqxNGuVFTM7p/rWlOib98vMLs/rMzpYU5aoiLcd4YwLDIUK+O+ekM95k+NI2oHFc4hhnKLABiS2D1gYhRs6uzNX0a1lhWcnwhM9/dQCNHUwy5ZvTldDOBQKIVp+XEWrBaGw0=";


        cachedProperties.put(p, gp.getProperties().get("textures").iterator().next());
        gp.getProperties().clear();
        gp.getProperties().put("textures", new Property("textures", texture, signature));
    }

    public void refresh(Player p) {

        eventPlayerManager.getEventPlayers().forEach(ep -> {
            p.hidePlayer(ep.getBukkitPlayer());
            p.showPlayer(ep.getBukkitPlayer());

            ep.getBukkitPlayer().hidePlayer(p);
            ep.getBukkitPlayer().showPlayer(p);
        });
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

        private final ItemStack item;
        private final int slot;

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
    public void onJoin(Player p)
    {
        clear(p);
        disguise(p);
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

        e.setDeathMessage(null);
        e.getDrops().clear();

        if (alive == 1) {
            p.spigot().respawn();

            ePlayers.forEach(ePlayer -> ePlayer.getBukkitPlayer().teleport(playableMap.getFallbackLocation()));


            CheckTimer ct = new CheckTimer(TimerType.CHECK, 1);

            ct.execute(() -> {
                ePlayers.forEach(this::onFinish);
                Core.i.getTextUtil().sendToAll("&6&lTribusMC &8» &a&l" + killer.getName() + " vann " + eventName + " eventet!");
            });

            finish();

        } else {
            p.spigot().respawn();
            p.setGameMode(GameMode.ADVENTURE);
            p.setAllowFlight(true);
            p.setFlying(true);




            Core.i.getTextUtil().sendTitleMessage("&c&lEliminerad!", 5, 25, 5, p);
        }


    }


    @Override
    protected void onPlayerDamageEvent(EntityDamageEvent e, Player p) {

    }

    @Override
    protected void onPlayerDamagePlayer(EntityDamageByEntityEvent e, Player p) {
        if(hasJoined(p)) {
            e.setCancelled(!isState(EventState.RUNNING));
        }
    }



    public void changeNameTag(Player p) {
        try {
            EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
            entityPlayer.displayName = "Test";

            eventPlayerManager.getEventPlayers().forEach(ep -> {
                if(ep.getBukkitPlayer() != p) {
                    ((CraftPlayer)ep.getBukkitPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(((CraftPlayer)p).getHandle()));

                }
            });

        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    public void hideNametag(Player player) {
        ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setMetadata("HideNametag", new FixedMetadataValue(Core.i, true)); //Optional
        player.setPassenger(stand);
    }

    public void resetNametag(Player player) {
        Entity entity = player.getPassenger();
        if (entity.hasMetadata("HideNametag")) {
            entity.remove();
        }
        //entity#remove();
    }

}
