package com.beelspace.beelsync.commands.beelsync;

import com.beelspace.beelsync.BeelSync;
import com.beelspace.beelsync.commands.AbstractCommand;
import com.beelspace.beelsync.commands.beelsync.subcommands.ReloadSubCMD;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BeelSyncCMD extends AbstractCommand {
    public BeelSyncCMD(BeelSync beelAuction) {
        super(beelAuction, "beelsync", "beelsync.admin", true, new ReloadSubCMD(beelAuction));
    }

    @Override
    protected void execute(CommandSender sender, @NotNull String[] args) {}

    @Override
    protected List<String> suggest(CommandSender sender, @NotNull String[] args) {
        if (args.length == 0) return List.of("reload");
        return List.of();
    }
}
