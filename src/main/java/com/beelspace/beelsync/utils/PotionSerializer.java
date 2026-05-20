package com.beelspace.beelsync.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PotionSerializer {
    public static String serializePotionEffects(List<PotionEffect> effects) {
        if (effects == null || effects.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < effects.size(); i++) {
            PotionEffect effect = effects.get(i);
            if (i > 0) sb.append(";");

            sb.append(effect.getType().getKey().toString())
                    .append(":")
                    .append(effect.getDuration())
                    .append(":")
                    .append(effect.getAmplifier())
                    .append(":")
                    .append(effect.isAmbient())
                    .append(":")
                    .append(effect.hasParticles())
                    .append(":")
                    .append(effect.hasIcon());
        }
        return sb.toString();
    }

    public static List<PotionEffect> deserializePotionEffects(String data) {
        final List<PotionEffect> effects = new ArrayList<>();
        if (data == null || data.isEmpty()) return effects;

        for (String part : data.split(";")) {
            String[] parts = part.split(":");
            if (parts.length < 4) continue;

            try {
                PotionEffectType type = Registry.EFFECT.get(NamespacedKey.minecraft(parts[1]));
                if (type == null) continue;

                int duration = Integer.parseInt(parts[2]);
                int amplifier = Integer.parseInt(parts[3]);
                boolean ambient = parts.length > 4 && Boolean.parseBoolean(parts[4]);
                boolean particles = parts.length > 5 && Boolean.parseBoolean(parts[5]);
                boolean icon = parts.length > 6 && Boolean.parseBoolean(parts[6]);

                effects.add(new PotionEffect(type, duration, amplifier, ambient, particles, icon));
            } catch (Exception ignored) {}
        }

        return effects;
    }
}