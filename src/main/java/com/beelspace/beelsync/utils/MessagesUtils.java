package com.beelspace.beelsync.utils;

import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MessagesUtils {
    public static void sendMessages(final CommandSender sender, final List<String> messages, final Map<String, String> replace) {
        final List<String> msgs = Lists.newArrayList();
        messages.forEach(s -> {
            AtomicReference<String> msg = new AtomicReference<>(s);
            if (replace != null && !replace.isEmpty()) {
                replace.forEach((s1, s2) -> msg.set(msg.get().replace(s1, s2)));
            }
            msgs.add(msg.get());
        });
        msgs.forEach(message -> sender.sendMessage(HexColor.toHex(message)));
    }

    public static void sendMessages(final CommandSender sender, final List<String> messages) {
        messages.forEach(message -> sender.sendMessage(HexColor.toHex(message)));
    }

    public static List<String> getReplacements(final List<String> messages, final Map<String, String> replace) {
        final List<String> msgs = Lists.newArrayList();
        messages.forEach(s -> {
            AtomicReference<String> msg = new AtomicReference<>(s);
            if (replace != null && !replace.isEmpty()) {
                replace.forEach((s1, s2) -> msg.set(msg.get().replace(s1, s2)));
            }
            msgs.add(msg.get());
        });

        return msgs;
    }
}
