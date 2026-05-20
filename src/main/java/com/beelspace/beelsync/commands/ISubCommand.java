package com.beelspace.beelsync.commands;

import org.bukkit.command.CommandSender;

public interface ISubCommand {
    void execute(CommandSender sender, String[] args);
}
