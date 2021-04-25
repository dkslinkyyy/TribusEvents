package me.slinng.tribusevent.config;


import me.slinng.tribusevent.Core;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ConfigLoader {

    private FileConfiguration fc;
    private final File file;
    private final String filename;
    private final Core core;

    public ConfigLoader(Core core, String filename) {
        this.core = core;
        this.filename = filename;
        this.file = new File(core.getDataFolder(), filename + ".yml");


    }


    public void UTF8_rewrite() throws IOException, InvalidConfigurationException {
        fc.load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

        Writer fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
        fileWriter.write(fc.saveToString());
        fileWriter.close();
    }

    public String getFileName() {
        return filename;
    }


    public void reloadCustomConfig() {
        if (!file.exists()) {
            core.saveResource(filename + ".yml", false);
        }
        fc = YamlConfiguration.loadConfiguration(file);
        if (updateConfig()) {
            core.getLogger().info("Configuration file (" + file.getName() + ") has been updated with the latest values.");
        }

    }

/*
    public void loadAllConfigs() {
        for(ConfigLoader cl : configLoaders()) {
            cl.reloadCustomConfig();
        }
    }
    private me.slinng.config.ConfigLoader[] configLoaders() {
        return new me.slinng.config.ConfigLoader[]{new MessagesFile(core)};
    }


 */


    public FileConfiguration getCustomConfig() {
        if (fc == null) {
            reloadCustomConfig();
        }

        return fc;
    }

    public void saveConfig() {
        try {
            fc.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public boolean updateConfig() {
        // Get the latest version of the file from the jar.
        InputStream is = core.getResource(filename + ".yml");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        FileConfiguration includedFile = YamlConfiguration.loadConfiguration(reader);
        // Load a copy of the current file to check for later.
        FileConfiguration originalFile = YamlConfiguration.loadConfiguration(file);
        // Loop through the values of the latest version and set any that are not present.
        for (String key : includedFile.getKeys(true)) {
            if (!(getCustomConfig().contains(key))) {
                getCustomConfig().set(key, includedFile.get(key));
            }
        }
        // Save the changes made, copy the default file.
        if (!(getCustomConfig().saveToString().equals(originalFile.saveToString()))) {
            try {
                fc.save(file);
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

}
