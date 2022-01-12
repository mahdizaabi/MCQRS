package com.techbank.account.cmd.domain;

import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.cmd.api.commands.WithDrawFundsCommand;
import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithDrawEvent;
import com.techbank.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    private Boolean active;
    private double balance;

    public AccountAggregate(OpenAccountCommand openAccountCommand) {
        raiseEvent(AccountOpenedEvent.builder()
                .accountHolder(openAccountCommand.getAccountHolder())
                .accountType(openAccountCommand.getAccountType())
                .createedDate(new Date())
                .identidier(openAccountCommand.getIdentidier())
                .openingBalance(openAccountCommand.getOpeningBalance())
                .build());
    }

    //method is invokeed through reflexion; (reload the aggregator with event data)
    public void apply(AccountOpenedEvent accountOpenedEvent) {
        this.id = accountOpenedEvent.getIdentidier();
        this.active = true;
        this.balance = accountOpenedEvent.getOpeningBalance();
    }

    public void depositFund(double amount) {
        if (this.active) {
            throw new IllegalArgumentException("Funds cannot be deposited into a closed account");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit must be greated than 0");
        }
        raiseEvent(FundsDepositedEvent
                .builder()
                .identidier(this.id)
                .amount(amount).build());
    }

    public void apply(FundsDepositedEvent fundsDepositedEvent) {
        this.id = fundsDepositedEvent.getIdentidier();
        this.active = false;
        this.balance = fundsDepositedEvent.getAmount();
    }

    public void withDrawFunds(double amount) {
        if (this.active) {
            throw new IllegalArgumentException("Funds cannot be deposited into a closed account");
        }
        raiseEvent(FundsWithDrawEvent.builder()
                .identidier(this.id)
                .amount(amount)
                .build()
        );
    }


    public void apply(FundsWithDrawEvent fundsWithDrawEvent) {
        this.id = fundsWithDrawEvent.getIdentidier();
        this.active = true;
        this.balance = fundsWithDrawEvent.getAmount();
    }

    //Command handler ::::$$$ check it first
    public void closeAccount() {
        raiseEvent(AccountClosedEvent.builder().identidier(this.id).build());
    }

    //called behind the scence to update/reload the state of the Aggragate
    public void apply(AccountClosedEvent accountClosedEvent) {
        this.id = accountClosedEvent.getIdentidier();
        this.active = false;
    }

}
