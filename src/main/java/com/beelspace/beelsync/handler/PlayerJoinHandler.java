package com.beelspace.beelsync.handler;

import com.beelspace.beelsync.BeelSync;
import com.beelspace.beelsync.profile.PlayerSyncProfile;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinHandler implements Listener {
    private final BeelSync beelSync;
    public PlayerJoinHandler(BeelSync beelSync) {
        this.beelSync = beelSync;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        beelSync.getSyncManager().addPlayer(e.getPlayer());
        Bukkit.getScheduler().runTaskAsynchronously(beelSync, () -> {
            PlayerSyncProfile playerSyncProfile = beelSync.getRedisPool().getPlayerSyncProfile(e.getPlayer().getName());
            if (playerSyncProfile != null) {
                Bukkit.getScheduler().runTask(beelSync, playerSyncProfile::syncProfile);
            } else {
                playerSyncProfile = beelSync.getProfileStorage().getProfile(e.getPlayer().getName());
                if (playerSyncProfile != null) Bukkit.getScheduler().runTask(beelSync, playerSyncProfile::syncProfile);
                beelSync.getProfileStorage().createProfile(e.getPlayer());
            }

            beelSync.getSyncManager().removePlayer(e.getPlayer());
        });
    }
}
