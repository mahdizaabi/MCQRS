package com.techbank.account.cmd.api.commands.handlers;

import com.techbank.account.cmd.api.commands.CloseAccountCommand;
import com.techbank.account.cmd.api.commands.DepositFundsCommand;
import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.cmd.api.commands.WithDrawFundsCommand;
import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.cqrs.core.Handlers.EventSourcingHandler;

import com.techbank.cqrs.core.domain.AggregateRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountCommandHandler implements CommandHandler {
    @Autowired
    private EventSourcingHandler<AccountAggregate> eventSourcingHandler;

    @Override
    public void handle(OpenAccountCommand command) {
        var aggregate = new AccountAggregate();
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(DepositFundsCommand command) {
        var aggregate = eventSourcingHandler.getById(command.getIdentidier());
        aggregate.depositFund(command.getAmount());
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(WithDrawFundsCommand command) {
        var aggregate = new AccountAggregate();
        if(command.getAmount() > aggregate.getBalance()){
            throw new IllegalArgumentException("withdraw declined, insufficient funds!");
        }
        aggregate.withDrawFunds(command.getAmount());

        //save latest state of the aggregate
        eventSourcingHandler.save(aggregate);

    }

    @Override
    public void handle(CloseAccountCommand command) {
     var aggregate = eventSourcingHandler.getById(command.getIdentidier());
     aggregate.closeAccount();
     //save the latest state of the aggregate(not the aggregate itself, the latest state o
     eventSourcingHandler.save(aggregate);
    }
}
