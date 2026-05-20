package com.beelspace.beelsync.commands;

import com.beelspace.beelsync.BeelSync;
import com.beelspace.beelsync.config.impl.MessagesConfig;
import com.beelspace.beelsync.utils.MessagesUtils;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class AbstractCommand implements BasicCommand {
    protected final BeelSync beelSync;
    protected final boolean useSubCommands;
    private final String command;
    private final String permission;
    private List<String> aliases;
    private List<SubCommand> subCommands;

    public AbstractCommand(final BeelSync beelSync, @NotNull String command, String permission, boolean useSubCommands, SubCommand... subCommands) {
        this.beelSync = beelSync;
        this.command = command;
        this.permission = permission;
        this.useSubCommands = useSubCommands;
        if (useSubCommands) this.subCommands = List.of(subCommands);
    }

    public AbstractCommand(final BeelSync beelSync, @NotNull String command, String permission, List<String> aliases, boolean useSubCommands, SubCommand... subCommands) {
        this.beelSync = beelSync;
        this.command = command;
        this.permission = permission;
        this.aliases = aliases;
        this.useSubCommands = useSubCommands;
        if (useSubCommands) this.subCommands = List.of(subCommands);
    }

    public AbstractCommand register() {
        if (aliases == null)
            beelSync.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
                event.registrar().register(command, this);
            });
        else
            beelSync.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
                event.registrar().register(command, aliases, this);
            });
        return this;
    }

    @Override
    public void execute(@NotNull CommandSourceStack source, @NotNull String[] args) {
        if (!source.getSender().hasPermission(permission))
        {
            MessagesUtils.sendMessages(source.getSender(), MessagesConfig.MESSAGES.ERRORS.notEnoughPermission(source.getSender()));
            return;
        }
        if (!useSubCommands) {
            execute(source.getSender(), args);
            return;
        }

        if (args.length > 0) {
            SubCommand subCommand = getSubCommand(args[0]);
            if (subCommand != null) {
                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                subCommand.execute(source.getSender(), subArgs);
            }
            return;
        }

        execute(source.getSender(), args);
    }

    @Override
    public @Nullable String permission() {
        return permission;
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
        return suggest(commandSourceStack.getSender(), args);
    }

    protected abstract void execute(CommandSender sender, @NotNull String[] args);
    protected abstract List<String> suggest(CommandSender sender, @NotNull String[] args);

    private SubCommand getSubCommand(String subCommand) {
        return subCommands.stream()
                .filter(s -> s.getName().equals(subCommand))
                .findFirst()
                .orElse(null);
    }
}
