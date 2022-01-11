package com.techbank.account.cmd.infrastructure;


import com.techbank.cqrs.core.commands.BaseCommand;
import com.techbank.cqrs.core.commands.CommandHandlerMethod;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {
    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        var handlers = routes.computeIfAbsent(type, aClass -> new LinkedList<>());
        handlers.add(handler);
    }

    @Override
    public void send(BaseCommand command) {

        var handlers = routes.get(command.getClass());
        if(handlers == null || handlers.size() == 0) {
            throw new RuntimeException("cannot find a command handler registred for this command ");

        }
        if (handlers.size() > 1) {
            throw new RuntimeException("cannot send command to more than one handler!");
        }
        handlers.get(0).handle(command);
    }
}
