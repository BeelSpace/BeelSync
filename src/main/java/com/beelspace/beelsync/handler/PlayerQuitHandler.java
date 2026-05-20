package com.beelspace.beelsync.handler;

import com.beelspace.beelsync.BeelSync;
import com.beelspace.beelsync.inventory.SyncInventory;
import com.beelspace.beelsync.profile.PlayerSyncProfile;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PlayerQuitHandler implements Listener {
    private final BeelSync beelSync;
    public PlayerQuitHandler(BeelSync beelSync) {
        this.beelSync = beelSync;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        if (beelSync.getSyncManager().getPlayersInSync().contains(player)) beelSync.getSyncManager().getPlayersInSync().remove(player);
        else {
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
        }
    }
}
