package me.slinng.tribusevent.event.events.lms;

import com.cryptomorin.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.event.Event;
import me.slinng.tribusevent.event.EventOccasion;
import me.slinng.tribusevent.event.EventState;
import me.slinng.tribusevent.event.ePlayer.EPlayer;
import me.slinng.tribusevent.objects.PlayableMap;
import me.slinng.tribusevent.miscelleanous.timer.CheckTimer;
import me.slinng.tribusevent.miscelleanous.timer.TimerType;
import me.slinng.tribusevent.placeholders.PlaceholderImpl;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class LMS extends Event {

    private PlayableMap map;
    private final HashMap<Player, ArrayList<CachedItem>> cachedItems;
    private final HashMap<Player, Property> cachedProperties;
    private final HashMap<Player, Integer> kills;
    private Scoreboard scoreboard;
    private Team disguisedTeam;
    private int alive = 0;


    public LMS() {
        super("LMS", 0, 36, EventState.UNREACHABLE, new EventOccasion("18:00"));

        setCheck(false);

        cachedItems = new HashMap<>();
        cachedProperties = new HashMap<>();
        kills = new HashMap<>();

        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        if (scoreboard.getTeam("disguised") == null) {
            disguisedTeam = scoreboard.registerNewTeam("disguised");
        } else {
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
        GameProfile gp = ((CraftPlayer) p).getProfile();

        if (cachedProperties.get(p) != null) {
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

    private static class CachedItem {

        private final String name;
        private final ItemStack item;
        private final int slot;

        CachedItem(String paramName, ItemStack paramItem, int paramSlot) {
            this.name = paramName;
            this.item = paramItem;
            this.slot = paramSlot;
        }


        public String getName() {
            return name;
        }
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

        for (int i = 0; i < 10; i++) {
            playerINV.setItem(i, splash);
        }
        playerINV.setItem(0, XMaterial.IRON_SWORD.parseItem());

    }

    private void unEquip(Player p) {
        p.getInventory().clear();

        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
    }


    private void cacheItems(Player p) {
        cachedItems.put(p, new ArrayList<>());
        for (int i = 0; i < p.getInventory().getSize(); i++) {
            if (p.getInventory().getItem(i) == null || p.getInventory().getItem(i).getType() == Material.AIR) continue;

            cachedItems.get(p).add(new CachedItem("item", p.getInventory().getItem(i), i));
        }

        for (ItemStack armor : p.getInventory().getArmorContents()) {
            if (armor == null || armor.getType() == Material.AIR) continue;

            cachedItems.get(p).add(new CachedItem("armor", armor, 0));

        }
    }

    private void fetchCachedItems(Player p) {
        ArrayList<CachedItem> playerCachedItems = cachedItems.get(p);

        if(playerCachedItems == null) return;

        playerCachedItems.forEach(cached -> {


            if(cached.getName().equalsIgnoreCase("armor")) {
                String typeName = cached.item.getType().name();
                if (typeName.endsWith("_HELMET"))
                    p.getInventory().setHelmet(cached.item);
                if (typeName.endsWith("_CHESTPLATE"))
                    p.getInventory().setChestplate(cached.item);
                if (typeName.endsWith("_LEGGINGS"))
                    p.getInventory().setLeggings(cached.item);
                if (typeName.endsWith("_BOOTS"))
                    p.getInventory().setBoots(cached.item);
            }else if(cached.getName().equalsIgnoreCase("item")){
                p.getInventory().setItem(cached.slot, cached.item);
            }

        });
    }

    private boolean isInventoryFull(Player p) {
        return p.getInventory().firstEmpty() == -1;
    }

    private void fillPots(Player p) {
        if (isInventoryFull(p)) return;

        PlayerInventory playerINV = p.getInventory();


        ItemStack splash = XMaterial.POTION.parseItem();
        assert splash != null;
        splash.setDurability((short) 16421);

        for (int i = 0; i < p.getInventory().getSize(); i++) {
            playerINV.setItem(i, splash);
        }
        playerINV.setItem(0, XMaterial.IRON_SWORD.parseItem());
    }




    @Override
    public void onJoin(Player p) {
        cacheItems(p);
        unEquip(p);
        kills.put(p, 0);


    }


    @Override
    protected void onStart(List<EPlayer> ePlayers) {
        alive = ePlayers.size();
        ePlayers.forEach(ePlayer -> {
            equip(ePlayer.getBukkitPlayer());
            disguise(ePlayer.getBukkitPlayer());
            refresh(ePlayer.getBukkitPlayer());
        });


    }

    @Override
    public void onFinish(List<EPlayer> ePlayers) {
        ePlayers.forEach(ep -> {
            unEquip(ep.getBukkitPlayer());
            fetchCachedItems(ep.getBukkitPlayer());
            unDisguise(ep.getBukkitPlayer());
            refresh(ep.getBukkitPlayer());
        });

    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent e, Player p, Player killer, List<EPlayer> ePlayers) {
        if (!hasJoined(p)) {
            return;
        }

        kills.put(killer, kills.get(killer) +1);

        alive -= 1;

        e.getDrops().clear();

        if (alive == 1) {
            p.spigot().respawn();

            ePlayers.forEach(ePlayer -> {
                ePlayer.getBukkitPlayer().teleport(playableMap.getFallbackLocation());
                ePlayer.getBukkitPlayer().getActivePotionEffects().forEach(potionEffect -> {
                    ePlayer.getBukkitPlayer().removePotionEffect(potionEffect.getType());
                });
            });


            CheckTimer ct = new CheckTimer(TimerType.CHECK, 1);


            ct.execute(() -> {
                Core.i.getTextUtil().sendToAll("&6&lTribusMC &8» &a&l" + killer.getName() + " vann " + eventName + " eventet!");
            });

            super.finish();
            cachedProperties.clear();


        } else {
            p.setHealth(20.0);
            p.setFoodLevel(20);
            p.setGameMode(GameMode.ADVENTURE);
            p.setAllowFlight(true);
            p.setFlying(true);
            Core.i.getTextUtil().sendTitleMessage("&c&lEliminerad!", 5, 25, 5, p);
        }

    }


    @Override
    protected void onPlayerDamageEvent(EntityDamageEvent e, Player p) {
      //  if(isState(EventState.RUNNING) || isState(EventState.WAITING) && hasJoined(p)) e.setCancelled(true);
    }


    @Override
    protected void onPlayerDamagePlayer(EntityDamageByEntityEvent e, Player p) {
        if (hasJoined(p)) {
            e.setCancelled(isState(EventState.RUNNING));
        }
    }

    @Override
    protected PlaceholderImpl[] getPlaceholders() {

        return new PlaceholderImpl[] {
                new PlaceholderImpl() {
                    @Override
                    public String getPlaceholder() {
                        return "kills";
                    }

                    @Override
                    public String getResult(Player p) {
                        return String.valueOf(fetchPlayerKills(p));
                    }
                },
                new PlaceholderImpl() {
                    @Override
                    public String getPlaceholder() {
                        return "total";
                    }

                    @Override
                    public String getResult(Player p) {
                        return String.valueOf(getPlayersInEvent());
                    }
                },
                new PlaceholderImpl() {
                    @Override
                    public String getPlaceholder() {
                        return "alive";
                    }

                    @Override
                    public String getResult(Player p) {
                        return String.valueOf(alive);
                    }
                }
        };
    }


    public int fetchPlayerKills(Player p) {
        return kills.get(p);
    }

    public int getAlive() {
        return alive;
    }


}
