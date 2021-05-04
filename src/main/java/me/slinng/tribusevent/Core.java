package me.slinng.tribusevent;


import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import me.jasperjh.animatedscoreboard.AnimatedScoreboard;
import me.jasperjh.animatedscoreboard.AnimatedScoreboardAPI;
import me.jasperjh.animatedscoreboard.config.PlayerScoreboardFile;
import me.jasperjh.animatedscoreboard.objects.PlayerScoreboardTemplate;
import me.slinng.tribusevent.commands.EventsCommand;
import me.slinng.tribusevent.config.ConfigManager;
import me.slinng.tribusevent.event.*;
import me.slinng.tribusevent.listeners.PlayerQuitListener;
import me.slinng.tribusevent.objects.PlayableMap;
import me.slinng.tribusevent.miscelleanous.TextUtil;
import me.slinng.tribusevent.placeholders.TribusEventPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.List;
import java.util.UUID;

public class Core extends JavaPlugin {

    public static Core i;
    private Inventory events;

    private TextUtil textUtil;
    private EventManager eManager;
    private EventController eController;
    private ConfigManager configManager;
    private AnimatedScoreboardAPI animatedScoreboardAPI;

    private PlayableMap playableMap;

    static {
        ConfigurationSerialization.registerClass(PlayableMap.class);

    }

    @Override
    public void onEnable() {
        i = this;

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }


        configManager = new ConfigManager(this);
        configManager.loadAllConfigs();
        registerCommands();

        this.animatedScoreboardAPI = AnimatedScoreboard.loadAPI(this);

        this.eManager = new EventManager();
        this.eController = new EventController();

        this.textUtil = new TextUtil();

        try {
            configManager.getConfigFile().UTF8_rewrite();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);



        loadEvents();
    }

    @Override
    public void onDisable() {
        //unloadEvents();
    }

    public AnimatedScoreboardAPI getAnimatedScoreboardAPI() {
        return animatedScoreboardAPI;
    }

    public PlayableMap getPlayableMap() {
        return playableMap;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public EventController getEventController() {
        return eController;
    }

    public EventManager getEventManager() {
        return eManager;
    }

    private void loadEvents() {
        eManager.registerEvent(eController.getLMS());
    }

    private void unloadEvents() {
        eManager.unregisterEvent(eController.getLMS());
    }

    private boolean hasChecked = false;

    private void checkEvents() {

        new BukkitRunnable() {

            @Override
            public void run() {
                for (Event event : eManager.getEvents()) {

                    if (hasChecked) {
                        hasChecked = false;
                        continue;
                    }
                    if (event.getOccasion().isEventReadyToStart()) {
                        try {
                            event.beginSearchForPlayers();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                    hasChecked = true;

                }

            }
        }.runTaskTimer(this, 20 * 3, 20L);

    }

    private void registerPlaceholders() {

    }


    @SuppressWarnings("unchecked")
    public static <T extends List<?>> T cast(Object obj) {
        return (T) obj;
    }

    public void openEventsGUI(Player p) {
        p.openInventory(events);
    }

    private void registerCommands() {
        new EventsCommand();
    }

    public TextUtil getTextUtil() {
        return textUtil;
    }

    public String trans(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
