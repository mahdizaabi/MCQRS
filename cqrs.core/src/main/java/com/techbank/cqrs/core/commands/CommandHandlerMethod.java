package com.techbank.cqrs.core.commands;

@FunctionalInterface
public interface CommandHandlerMethod<T extends BaseCommand> {
    public void handle(T command);
}
