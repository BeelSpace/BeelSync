package com.beelspace.beelsync.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Base64;

public class ItemStackSerializer {
    public static String serialize(ItemStack item) {
        if (item == null || item.getType() == Material.AIR || item.getAmount() == 0) {
            return "EMPTY_ITEM";
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

        if (base64.equals("EMPTY_ITEM")) {
            return new ItemStack(Material.AIR, 0);
        }

        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            ItemStack item = ItemStack.deserializeBytes(bytes);

            if (item == null || item.getType() == Material.AIR || item.getAmount() == 0) {
                return new ItemStack(Material.AIR, 0);
            }

            return item;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
