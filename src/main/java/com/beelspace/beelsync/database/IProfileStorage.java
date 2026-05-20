package com.beelspace.beelsync.database;

import com.beelspace.beelsync.profile.PlayerSyncProfile;
import org.bukkit.entity.Player;

import java.util.List;

public interface IProfileStorage {
    List<PlayerSyncProfile> getProfiles();
    PlayerSyncProfile getProfile(final String nickPlayer);
    PlayerSyncProfile createProfile(final Player player);
    void updateProfile(final PlayerSyncProfile profile);
}
