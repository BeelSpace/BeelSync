package com.beelspace.beelsync.handler;

import com.beelspace.beelsync.BeelSync;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerSyncHandler implements Listener {
    private final BeelSync beelSync;
    public PlayerSyncHandler(BeelSync beelSync) {
        this.beelSync = beelSync;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        final Player player = e.getPlayer();
        if (beelSync.getSyncManager().getPlayersInSync().contains(player)) e.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        final Player player = (Player) e.getWhoClicked();
        if (beelSync.getSyncManager().getPlayersInSync().contains(player)) e.setCancelled(true);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        final Player player = e.getPlayer();
        if (beelSync.getSyncManager().getPlayersInSync().contains(player)) e.setCancelled(true);
    }
}
