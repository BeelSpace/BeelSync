package com.beelspace.beelsync.config;

import com.beelspace.beelsync.BeelSync;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private final List<AbstractConfig> configs = new ArrayList<>();
    public void registerConfig(AbstractConfig config) {
        configs.add(config);
    }

    public void clearConfigs() {
        configs.clear();
    }

    public void reloadAll() {
        configs.forEach(AbstractConfig::reloadConfig);
    }

    public AbstractConfig getConfig(String name) {
        return configs.stream()
                .filter(config -> config.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

}
