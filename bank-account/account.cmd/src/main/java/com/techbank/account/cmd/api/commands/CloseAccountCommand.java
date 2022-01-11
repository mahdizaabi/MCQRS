package com.techbank.account.cmd.api.commands;

import com.techbank.cqrs.core.commands.BaseCommand;

public class CloseAccountCommand extends BaseCommand {
    private String iiid;//Not used inside this class, onl
    public CloseAccountCommand(String id) {
        super(id);
        iiid = id;//don't need iteven
    }
}

