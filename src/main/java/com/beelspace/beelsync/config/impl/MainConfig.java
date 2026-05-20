package com.beelspace.beelsync.config.impl;

import com.beelspace.beelsync.BeelSync;
import com.beelspace.beelsync.config.AbstractConfig;
import lombok.experimental.UtilityClass;

public class MainConfig extends AbstractConfig {
    public MainConfig(BeelSync beelSync) {
        super(beelSync, "config");
    }

    @Override
    public MainConfig init() {
        REDIS.host = getConfig().getString("redis.host");
        REDIS.port = getConfig().getInt("redis.port");
        REDIS.timeout = getConfig().getInt("redis.timeout");
        REDIS.timeSynchronization = getConfig().getInt("redis.time-synchronization")*20;
        REDIS.isEnableSynchronization = getConfig().getBoolean("redis.is-enable-synchronization");
        REDIS.user = getConfig().getString("redis.user");
        REDIS.password = getConfig().getString("redis.password");
        REDIS.database = getConfig().getInt("redis.database");

        CASH.update = getConfig().getInt("cash.update")*20;

        DATABASE.host = getConfig().getString("database.host");
        DATABASE.port = getConfig().getInt("database.port");
        DATABASE.database = getConfig().getString("database.database");
        DATABASE.user = getConfig().getString("database.user");
        DATABASE.password = getConfig().getString("database.password");
        return this;
    }

    @UtilityClass
    public static class REDIS {
        public String host;
        public int port;
        public int timeout;
        public int timeSynchronization;
        public boolean isEnableSynchronization;
        public String user;
        public String password;
        public int database;
    }

    @UtilityClass
    public static class CASH {
        public int update;
    }

    @UtilityClass
    public static class DATABASE {
        public String host;
        public int port;
        public String database;
        public String user;
        public String password;
    }
}