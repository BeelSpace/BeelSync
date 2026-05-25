package com.beelspace.beelsync;

import com.beelspace.beelsync.commands.beelsync.BeelSyncCMD;
import com.beelspace.beelsync.config.ConfigManager;
import com.beelspace.beelsync.config.impl.MainConfig;
import com.beelspace.beelsync.config.impl.MessagesConfig;
import com.beelspace.beelsync.database.IDataBase;
import com.beelspace.beelsync.database.IProfileStorage;
import com.beelspace.beelsync.database.mysql.MySqlDB;
import com.beelspace.beelsync.database.mysql.MySqlProfileStorage;
import com.beelspace.beelsync.handler.PlayerJoinHandler;
import com.beelspace.beelsync.handler.PlayerQuitHandler;
import com.beelspace.beelsync.handler.PlayerSyncHandler;
import com.beelspace.beelsync.profile.SyncManager;
import com.beelspace.beelsync.redis.RedisPool;
import com.dreamcode.dreamthemes.api.ThemeAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

@Getter
public final class BeelSync extends JavaPlugin {
    @Getter
    private static BeelSync instance;
    private IDataBase dataBase;
    private IProfileStorage profileStorage;
    private RedisPool redisPool;

    private ConfigManager configManager;
    private SyncManager syncManager;

    private ThemeAPI themeAPI;

    @Override
    public void onEnable() {
        instance = this;
        registerDependencies();
        registerConfigs();
        registerDataBase();
        registerHandlers();
        registerCommands();
    }

    @Override
    public void onDisable() {
        unregisterDataBase();
    }

    private void registerDataBase() {
        dataBase = new MySqlDB(
                MainConfig.DATABASE.host,
                MainConfig.DATABASE.port,
                MainConfig.DATABASE.database,
                MainConfig.DATABASE.user,
                MainConfig.DATABASE.password
        );
        dataBase.create();

        profileStorage = new MySqlProfileStorage(dataBase);
        syncManager = new SyncManager(this);
        syncManager.checkPlayerInSync();
        syncManager.startUpdateInRedis();

        redisPool = new RedisPool(this,
                MainConfig.REDIS.host,
                MainConfig.REDIS.port,
                MainConfig.REDIS.timeout,
                MainConfig.REDIS.user,
                MainConfig.REDIS.password,
                MainConfig.REDIS.database
        );
        redisPool.loadProfiles();
        redisPool.saveDataInDatabase();
    }


    private void registerDependencies() {
        final Plugin dreamThemes = getServer().getPluginManager().getPlugin("DreamThemes");
        if (dreamThemes != null && dreamThemes.isEnabled()) themeAPI = getServer().getServicesManager().load(ThemeAPI.class);
    }

    private void registerConfigs() {
        final MainConfig mainConfig = new MainConfig(this).init();
        final MessagesConfig messagesConfig = new MessagesConfig(this).init();

        configManager = new ConfigManager();
        configManager.registerConfig(mainConfig);
        configManager.registerConfig(messagesConfig);
    }

    private void registerHandlers() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerSyncHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitHandler(this), this);
    }

    private void registerCommands() {
        new BeelSyncCMD(this).register();
    }

    private void unregisterDataBase() {
        try {
            dataBase.getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
