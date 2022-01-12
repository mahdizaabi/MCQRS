package com.techbank.account.cmd;


import com.techbank.account.cmd.api.commands.CloseAccountCommand;
import com.techbank.account.cmd.api.commands.DepositFundsCommand;
import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.cmd.api.commands.WithDrawFundsCommand;
import com.techbank.account.cmd.api.commands.handlers.CommandHandler;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CommandApplication {
    @Autowired
    private CommandDispatcher commandDispatcher;
    @Autowired
    private CommandHandler commandHandler;

    public static void main(String[] args) {
        SpringApplication.run(CommandApplication.class, args);
    }

    @PostConstruct
    public void registerhandler() {
        commandDispatcher.registerHandler(OpenAccountCommand.class, commandHandler::handle);
        commandDispatcher.registerHandler(CloseAccountCommand.class, commandHandler::handle);
        commandDispatcher.registerHandler(WithDrawFundsCommand.class, commandHandler::handle);
        commandDispatcher.registerHandler(DepositFundsCommand.class, commandHandler::handle);
    }
}
