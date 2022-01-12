package com.techbank.account.cmd.api.commands.handlers;

import com.techbank.account.cmd.api.commands.CloseAccountCommand;
import com.techbank.account.cmd.api.commands.DepositFundsCommand;
import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.cmd.api.commands.WithDrawFundsCommand;

public interface CommandHandler {

    void handle(OpenAccountCommand command);

    void handle(DepositFundsCommand command);

    void handle(WithDrawFundsCommand command);

    void handle(CloseAccountCommand command);
}
