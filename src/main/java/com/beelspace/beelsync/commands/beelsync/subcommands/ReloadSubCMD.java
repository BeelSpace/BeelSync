package com.beelspace.beelsync.commands.beelsync.subcommands;

import com.beelspace.beelsync.BeelSync;
import com.beelspace.beelsync.commands.SubCommand;
import com.beelspace.beelsync.config.impl.MessagesConfig;
import com.beelspace.beelsync.utils.MessagesUtils;
import org.bukkit.command.CommandSender;

public class ReloadSubCMD extends SubCommand {
    public ReloadSubCMD(BeelSync beelSync) {
        super(beelSync, "reload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        getBeelSync().getConfigManager().reloadAll();
        MessagesUtils.sendMessages(sender, MessagesConfig.MESSAGES.configReload(sender));
    }
}
