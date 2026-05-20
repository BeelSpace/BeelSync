package com.beelspace.beelsync.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Base64;

public class ItemStackSerializer {
    public static String serialize(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }

        try {
            byte[] bytes = item.serializeAsBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack deserialize(String base64) {
        if (base64 == null || base64.isEmpty()) {
            return null;
        }

        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            return ItemStack.deserializeBytes(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
