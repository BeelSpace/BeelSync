package com.beelspace.beelsync.profile;

import com.beelspace.beelsync.BeelSync;
import com.beelspace.beelsync.config.impl.MainConfig;
import com.beelspace.beelsync.config.impl.MessagesConfig;
import com.beelspace.beelsync.inventory.SyncInventory;
import com.beelspace.beelsync.utils.MessagesUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SyncManager {
    private final BeelSync beelSync;
    public SyncManager(BeelSync beelSync) {
        this.beelSync = beelSync;
    }
    @Getter
    private final List<Player> playersInSync = new ArrayList<>();

    public void addPlayer(Player player) {
        playersInSync.add(player);
    }
    public void removePlayer(Player player) {
        playersInSync.remove(player);
    }

    public void checkPlayerInSync() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(beelSync, () -> {
            getPlayersInSync().forEach(player -> {
                MessagesUtils.sendMessages(player, MessagesConfig.MESSAGES.dataIsSync(player.getName()));
            });
        }, 0, 20);
    }

    public void startUpdateInRedis() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(beelSync, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                final PlayerSyncProfile playerSyncProfile = new PlayerSyncProfile(
                        player.getName(),
                        new SyncInventory(player.getInventory()),
                        player.getActivePotionEffects().stream().toList(),
                        player.getHealth(),
                        player.getHealthScale(),
                        player.getAttribute(Attribute.MAX_HEALTH).getBaseValue(),
                        player.getTotalExperience(),
                        player.getFoodLevel(),
                        player.getSaturation(),
                        player.getExhaustion()
                );
                beelSync.getRedisPool().setPlayerSyncProfile(playerSyncProfile);
            });
        }, 0, MainConfig.CASH.update);
    }
}
