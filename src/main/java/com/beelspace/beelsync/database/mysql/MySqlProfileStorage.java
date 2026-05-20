package com.beelspace.beelsync.database.mysql;

import com.beelspace.beelsync.database.IDataBase;
import com.beelspace.beelsync.database.IProfileStorage;
import com.beelspace.beelsync.inventory.SyncInventory;
import com.beelspace.beelsync.profile.PlayerSyncProfile;
import com.beelspace.beelsync.utils.PotionSerializer;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlProfileStorage implements IProfileStorage {

    private final IDataBase database;

    public MySqlProfileStorage(final IDataBase iDataBase) {
        this.database = iDataBase;
    }

    @Override
    public List<PlayerSyncProfile> getProfiles() {
        final List<PlayerSyncProfile> profiles = new ArrayList<>();

        try (Connection connection = database.getConnection();
             PreparedStatement selectAll = connection.prepareStatement(MySqlRequests.GET_PROFILES.getRequest());
             ResultSet resultSet = selectAll.executeQuery()) {

            while (resultSet.next()) {
                profiles.add(new PlayerSyncProfile(
                        resultSet.getString("nick_player"),
                        SyncInventory.deserialize(resultSet.getString("inventory")),
                        PotionSerializer.deserializePotionEffects(resultSet.getString("potion_effects")),
                        resultSet.getDouble("health"),
                        resultSet.getDouble("health_scale"),
                        resultSet.getDouble("max_health"),
                        resultSet.getInt("experience"),
                        resultSet.getInt("food_level"),
                        resultSet.getFloat("saturation"),
                        resultSet.getFloat("exhaustion")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return profiles;
    }

    @Override
    public PlayerSyncProfile getProfile(String nickPlayer) {
        try (Connection connection = database.getConnection();
             PreparedStatement selectInfo = connection.prepareStatement(MySqlRequests.GET_PROFILE.getRequest())) {

            selectInfo.setString(1, nickPlayer);

            try (ResultSet resultInfo = selectInfo.executeQuery()) {
                if (resultInfo.next()) {
                    return new PlayerSyncProfile(
                            nickPlayer,
                            SyncInventory.deserialize(resultInfo.getString("inventory")),
                            PotionSerializer.deserializePotionEffects(resultInfo.getString("potion_effects")),
                            resultInfo.getDouble("health"),
                            resultInfo.getDouble("health_scale"),
                            resultInfo.getDouble("max_health"),
                            resultInfo.getInt("experience"),
                            resultInfo.getInt("food_level"),
                            resultInfo.getFloat("saturation"),
                            resultInfo.getFloat("exhaustion")
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayerSyncProfile createProfile(final Player player) {
        final PlayerSyncProfile playerSyncProfile = new PlayerSyncProfile(
                player.getName(),
                new SyncInventory(player.getInventory()),
                new ArrayList<>(player.getActivePotionEffects()),
                player.getHealth(),
                player.getHealthScale(),
                player.getAttribute(Attribute.MAX_HEALTH).getBaseValue(),
                player.getTotalExperience(),
                player.getFoodLevel(),
                player.getSaturation(),
                player.getExhaustion()
        );

        try (Connection connection = database.getConnection();
             PreparedStatement insertInfo = connection.prepareStatement(MySqlRequests.CREATE_PROFILE.getRequest())) {

            insertInfo.setString(1, player.getName());
            insertInfo.setString(2, playerSyncProfile.getSyncInventory().serialize());
            insertInfo.setString(3, PotionSerializer.serializePotionEffects(playerSyncProfile.getActivePotionEffects()));
            insertInfo.setDouble(4, playerSyncProfile.getHealth());
            insertInfo.setDouble(5, playerSyncProfile.getHealthScale());
            insertInfo.setDouble(6, playerSyncProfile.getMaxHealth());
            insertInfo.setInt(7, playerSyncProfile.getExperience());
            insertInfo.setInt(8, playerSyncProfile.getFoodLevel());
            insertInfo.setFloat(9, playerSyncProfile.getSaturation());
            insertInfo.setFloat(10, playerSyncProfile.getExhaustion());
            insertInfo.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return playerSyncProfile;
    }

    @Override
    public void updateProfile(PlayerSyncProfile playerSyncProfile) {
        try (Connection connection = database.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(MySqlRequests.UPDATE_PROFILE.getRequest())) {

            updateStatement.setString(1, playerSyncProfile.getSyncInventory().serialize());
            updateStatement.setString(2, PotionSerializer.serializePotionEffects(playerSyncProfile.getActivePotionEffects()));
            updateStatement.setDouble(3, playerSyncProfile.getHealth());
            updateStatement.setDouble(4, playerSyncProfile.getHealthScale());
            updateStatement.setDouble(5, playerSyncProfile.getMaxHealth());
            updateStatement.setInt(6, playerSyncProfile.getExperience());
            updateStatement.setInt(7, playerSyncProfile.getFoodLevel());
            updateStatement.setFloat(8, playerSyncProfile.getSaturation());
            updateStatement.setFloat(9, playerSyncProfile.getExhaustion());
            updateStatement.setString(10, playerSyncProfile.getNickPlayer());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}