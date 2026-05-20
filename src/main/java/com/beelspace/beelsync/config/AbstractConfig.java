package com.beelspace.beelsync.config;

import com.beelspace.beelsync.BeelSync;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public abstract class AbstractConfig {
    private final BeelSync beelSync;
    private final String name;

    private File file;
    private FileConfiguration config;

    public AbstractConfig(final BeelSync beelSync, String name) {
        this.beelSync = beelSync;
        this.name = name;
        loadConfig();
    }

    public abstract AbstractConfig init();

    private void loadConfig() {
        file = new File(beelSync.getDataFolder(), name + ".yml");
        if(!file.exists()) {
            beelSync.saveResource(name + ".yml", true);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void reloadConfig()  {
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }

        init();
    }
}