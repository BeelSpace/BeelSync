package com.beelspace.beelsync.inventory;

import com.beelspace.beelsync.utils.ItemStackSerializer;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SyncInventory {
    private final Map<Integer, ItemStack> items;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack itemInOffHand;

    public SyncInventory(final Map<Integer, ItemStack> items, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack itemInOffHand) {
        this.items = items;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.itemInOffHand = itemInOffHand;
    }

    public SyncInventory(final PlayerInventory inventory) {
        this.items = new HashMap<>();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && !item.getType().isAir()) {
                this.items.put(i, item.clone());
            }
        }

        this.helmet = cloneIfNotNull(inventory.getHelmet());
        this.chestplate = cloneIfNotNull(inventory.getChestplate());
        this.leggings = cloneIfNotNull(inventory.getLeggings());
        this.boots = cloneIfNotNull(inventory.getBoots());
        this.itemInOffHand = cloneIfNotNull(inventory.getItemInOffHand());
    }

    public void setItem(int slot, ItemStack item) {
        items.put(slot, item);
    }

    public String serialize() {
        final Gson gson = new GsonBuilder().create();
        final Map<String, String> serializedData = new HashMap<>();
        final Map<String, String> serializedItems = new HashMap<>();

        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            String serializedItem = ItemStackSerializer.serialize(entry.getValue());
            if (serializedItem != null) {
                serializedItems.put(String.valueOf(entry.getKey()), serializedItem);
            }
        }
        serializedData.put("items", gson.toJson(serializedItems));

        serializedData.put("helmet", ItemStackSerializer.serialize(helmet));
        serializedData.put("chestplate", ItemStackSerializer.serialize(chestplate));
        serializedData.put("leggings", ItemStackSerializer.serialize(leggings));
        serializedData.put("boots", ItemStackSerializer.serialize(boots));
        serializedData.put("itemInOffHand", ItemStackSerializer.serialize(itemInOffHand));

        return gson.toJson(serializedData);
    }

    public static SyncInventory deserialize(final String serializedInventory) {
        Gson gson = new GsonBuilder().create();

        @SuppressWarnings("unchecked")
        Map<String, String> serializedData = gson.fromJson(serializedInventory, Map.class);

        @SuppressWarnings("unchecked")
        Map<String, String> serializedItems = gson.fromJson(serializedData.get("items"), Map.class);

        Map<Integer, ItemStack> items = new HashMap<>();
        if (serializedItems != null) {
            for (Map.Entry<String, String> entry : serializedItems.entrySet()) {
                ItemStack item = ItemStackSerializer.deserialize(entry.getValue());
                if (item != null) {
                    items.put(Integer.parseInt(entry.getKey()), item);
                }
            }
        }

        ItemStack helmet = ItemStackSerializer.deserialize(serializedData.get("helmet"));
        ItemStack chestplate = ItemStackSerializer.deserialize(serializedData.get("chestplate"));
        ItemStack leggings = ItemStackSerializer.deserialize(serializedData.get("leggings"));
        ItemStack boots = ItemStackSerializer.deserialize(serializedData.get("boots"));
        ItemStack itemInOffHand = ItemStackSerializer.deserialize(serializedData.get("itemInOffHand"));

        return new SyncInventory(items, helmet, chestplate, leggings, boots, itemInOffHand);
    }

    private ItemStack cloneIfNotNull(ItemStack item) {
        return (item != null && !item.getType().isAir()) ? item.clone() : null;
    }
}