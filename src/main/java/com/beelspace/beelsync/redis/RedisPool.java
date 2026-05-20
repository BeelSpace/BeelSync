package com.beelspace.beelsync.redis;

import com.beelspace.beelsync.BeelSync;
import com.beelspace.beelsync.config.impl.MainConfig;
import com.beelspace.beelsync.inventory.SyncInventory;
import com.beelspace.beelsync.profile.PlayerSyncProfile;
import com.beelspace.beelsync.utils.PotionSerializer;
import com.beelspace.beelsync.utils.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisPool {
    private final JedisPool pool;
    private final Gson gson = new Gson();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final BeelSync beelSync;

    public RedisPool(BeelSync beelSync, String host, int port, int timeout, String user, String password, int database) {
        this.beelSync = beelSync;

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(16);
        poolConfig.setMaxIdle(8);

        pool = new JedisPool(poolConfig, host, port, timeout, password, database, user);
    }


    public void loadProfiles() {
        if (!hasProfiles()) {
            Bukkit.getScheduler().runTaskAsynchronously(beelSync, () -> {
                beelSync.getProfileStorage().getProfiles().forEach(this::setPlayerSyncProfile);
            });
        }
    }

    public boolean hasProfiles() {
        return getPlayerSyncProfiles() != null && !getPlayerSyncProfiles().isEmpty();
    }

    public void setPlayerSyncProfile(PlayerSyncProfile playerSyncProfile) {
        try (Jedis jedis = pool.getResource()) {
            final Map<String, Object> data = new HashMap<>();
            data.put("inventory", playerSyncProfile.getSyncInventory().serialize());
            data.put("potionEffects", PotionSerializer.serializePotionEffects(playerSyncProfile.getActivePotionEffects()));
            data.put("health", playerSyncProfile.getHealth());
            data.put("healthScale", playerSyncProfile.getHealthScale());
            data.put("maxHealth", playerSyncProfile.getMaxHealth());
            data.put("experience", playerSyncProfile.getExperience());
            data.put("foodLevel", playerSyncProfile.getFoodLevel());
            data.put("saturation", playerSyncProfile.getSaturation());
            data.put("exhaustion", playerSyncProfile.getExhaustion());

            jedis.hset(RedisKeys.PROFILE.getKey(), playerSyncProfile.getNickPlayer(), gson.toJson(data));
        }
    }

    public PlayerSyncProfile getPlayerSyncProfile(String playerName) {
        try (Jedis jedis = pool.getResource()) {
            String json = jedis.hget(RedisKeys.PROFILE.getKey(), playerName);
            if (json == null) return null;

            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            return new PlayerSyncProfile(
                    playerName,
                    SyncInventory.deserialize(obj.get("inventory").getAsString()),
                    PotionSerializer.deserializePotionEffects(obj.get("potionEffects").getAsString()),
                    obj.get("health").getAsDouble(),
                    obj.get("healthScale").getAsDouble(),
                    obj.get("maxHealth").getAsDouble(),
                    obj.get("experience").getAsInt(),
                    obj.get("foodLevel").getAsInt(),
                    obj.get("saturation").getAsFloat(),
                    obj.get("exhaustion").getAsFloat()
            );
        }
    }

    public List<PlayerSyncProfile> getPlayerSyncProfiles() {
        final List<PlayerSyncProfile> profiles = new ArrayList<>();

        try (Jedis jedis = pool.getResource()) {
            final Map<String, String> map = jedis.hgetAll(RedisKeys.PROFILE.getKey());
            map.keySet().forEach(playerName -> {
                PlayerSyncProfile playerSyncProfile = getPlayerSyncProfile(playerName);
                if (playerSyncProfile != null) profiles.add(playerSyncProfile);
            });
        }

        return profiles;
    }

    public void saveDataInDatabase() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(beelSync, () -> {
            if (!TimeUtils.hasTimePassed(getLastTimeUpdate()) || !MainConfig.REDIS.isEnableSynchronization) return;
            setLastTimeUpdate();

            final List<PlayerSyncProfile> profiles = getPlayerSyncProfiles();
            profiles.forEach(profile -> beelSync.getProfileStorage().updateProfile(profile));
        }, 0, MainConfig.REDIS.timeSynchronization);
    }

    private void setLastTimeUpdate() {
        try (Jedis jedis = pool.getResource()) {
            LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
            String dateTimeString = localDateTime.format(formatter);
            jedis.set(RedisKeys.LAST_TIME_UPDATE.getKey(), dateTimeString);
        }
    }

    private LocalDateTime getLastTimeUpdate() {
        try (Jedis jedis = pool.getResource()) {
            String dateTimeString = jedis.get(RedisKeys.LAST_TIME_UPDATE.getKey());
            if (dateTimeString == null) return null;
            return LocalDateTime.parse(dateTimeString, formatter);
        }
    }
}
