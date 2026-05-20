package com.beelspace.beelsync.commands;

import com.beelspace.beelsync.BeelSync;
import lombok.Getter;

@Getter
public abstract class SubCommand implements ISubCommand {
    private final String name;
    private final BeelSync beelSync;

    public SubCommand(final BeelSync beelSync, String name) {
        this.beelSync = beelSync;
        this.name = name;
    }
}
