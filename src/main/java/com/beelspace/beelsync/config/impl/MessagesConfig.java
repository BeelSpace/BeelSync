package com.beelspace.beelsync.config.impl;

import com.beelspace.beelsync.BeelSync;
import com.beelspace.beelsync.config.AbstractConfig;
import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class MessagesConfig extends AbstractConfig {
    public MessagesConfig(BeelSync beelSync) {
        super(beelSync, "messages");
    }

    @Override
    public MessagesConfig init() {
        MESSAGES.config = getConfig();
        MESSAGES.beelSync = getBeelSync();
        return this;
    }

    @UtilityClass
    public static class MESSAGES {
        private FileConfiguration config;
        private BeelSync beelSync;

        public List<String> configReload(CommandSender sender) {
            if (sender instanceof Player) return config.getStringList("messages.config-reload." + beelSync.getThemeAPI().getTheme(sender.getName()));
            else return config.getStringList("messages.config-reload.CLASSIC");
        }

        public List<String> dataIsSync(String nickPlayer) {
            return config.getStringList("messages.data-is-sync." + beelSync.getThemeAPI().getTheme(nickPlayer));
        }

        @UtilityClass
        public static class ERRORS {
            public List<String> notEnoughPermission(CommandSender sender) {
                if (sender instanceof Player) return config.getStringList("messages.errors.not-enough-permission." + beelSync.getThemeAPI().getTheme(sender.getName()));
                else return config.getStringList("messages.errors.not-enough-permission.CLASSIC");
            }
        }
    }
}
