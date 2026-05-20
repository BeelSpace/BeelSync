package com.beelspace.beelsync.utils;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexColor {
    public static String toHex(String from) {
        Pattern pattern = Pattern.compile("&#[a-fA-F\0-9]{6}");
        Matcher matcher = pattern.matcher(from);
        while (matcher.find()) {
            String hexCode = from.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace("&#", "x");
            StringBuilder builder = new StringBuilder();
            for (char c : replaceSharp.toCharArray()) builder.append("&").append(c);
            from = from.replace(hexCode, builder.toString());
            matcher = pattern.matcher(from);
        }
        return ChatColor.translateAlternateColorCodes('&', from);
    }
}

