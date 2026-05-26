package com.beelspace.beelsync.profile;

import com.beelspace.beelsync.inventory.SyncInventory;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.List;

@Getter
public class PlayerSyncProfile {
    private final String nickPlayer;
    private final SyncInventory syncInventory;
    private final List<PotionEffect> activePotionEffects;

    private final double health;
    private final double healthScale;
    private final double maxHealth;

    private final int experience;

    private final int foodLevel;
    private final float saturation;
    private final float exhaustion;

    public PlayerSyncProfile(final String nickPlayer, final SyncInventory syncInventory, final List<PotionEffect> activePotionEffects,
                             final double health, final double healthScale, final double maxHealth, final int experience,
                             final int foodLevel, float saturation, float exhaustion) {
        this.nickPlayer = nickPlayer;
        this.syncInventory = syncInventory;
        this.activePotionEffects = activePotionEffects;

        this.health = health;
        this.healthScale = healthScale;
        this.maxHealth = maxHealth;

        this.experience = experience;

        this.foodLevel = foodLevel;
        this.saturation = saturation;
        this.exhaustion = exhaustion;
    };

    public void syncProfile() {
        final Player player = Bukkit.getPlayer(nickPlayer);
        if (player != null) {
            syncInventory(player);
            syncPotionEffects(player);
            syncFoodLevel(player);
            syncHealth(player);
            syncExperience(player);
        }
    }

    public void syncInventory(final Player player) {
        player.getInventory().clear();
        syncInventory.getItems().forEach((slot, itemStack) -> player.getInventory().setItem(slot, itemStack));
        player.getInventory().setHelmet(syncInventory.getHelmet());
        player.getInventory().setChestplate(syncInventory.getChestplate());
        player.getInventory().setLeggings(syncInventory.getLeggings());
        player.getInventory().setBoots(syncInventory.getBoots());
        player.getInventory().setItemInOffHand(syncInventory.getItemInOffHand());
        player.getInventory().setHeldItemSlot(syncInventory.getSlotOfMainHand());
        player.updateInventory();
    }

    private void syncPotionEffects(final Player player) {
        player.getActivePotionEffects().forEach(effect ->
                player.removePotionEffect(effect.getType())
        );
        player.addPotionEffects(activePotionEffects);
    }

    private void syncFoodLevel(final Player player) {
        player.setFoodLevel(foodLevel);
        player.setSaturation(saturation);
        player.setExhaustion(exhaustion);
    }

    private void syncHealth(final Player player) {
        final AttributeInstance maxHealthAttr = player.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealthAttr != null) {
            maxHealthAttr.setBaseValue(maxHealth);
        }

        player.setHealthScale(healthScale);
        player.setHealth(health);
    }

    private void syncExperience(final Player player) {
        player.setExp(0);
        player.setLevel(0);
        player.setTotalExperience(0);
        player.giveExp(experience);
    }
}
