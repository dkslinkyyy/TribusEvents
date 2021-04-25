package me.slinng.tribusevent.config;

import me.slinng.tribusevent.Core;
import me.slinng.tribusevent.config.ConfigLoader;
import me.slinng.tribusevent.config.configs.DefaultConfig;
import me.slinng.tribusevent.config.configs.MapsConfig;
import me.slinng.tribusevent.config.configs.RewardsConfig;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private final List<ConfigLoader> configs;


    public ConfigManager(Core core ) {
        this.configs = new ArrayList<>();
        configs.add(new DefaultConfig(core));
        configs.add(new MapsConfig(core));
        configs.add(new RewardsConfig(core));

    }

    public void loadAllConfigs() {
        configs.forEach(ConfigLoader::reloadCustomConfig);
    }

    public void reloadAllConfigs() {
        for(ConfigLoader cl : configs) {
            cl.reloadCustomConfig();
            cl.saveConfig();
        }

    }



    public DefaultConfig getConfigFile() {
        return (DefaultConfig) this.configs.get(0);
    }
    public MapsConfig getMapsConfig() {
        return (MapsConfig) this.configs.get(1);
    }
    public RewardsConfig getRewardsConfig() {
        return (RewardsConfig) this.configs.get(2);
    }



}
