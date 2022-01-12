package com.techbank.account.query.infrastructure.Handlers;


import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithDrawEvent;
import com.techbank.account.query.domain.AccountRepository;
import com.techbank.account.query.domain.BankAccount;
import com.techbank.account.query.infrastructure.Handlers.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountEventHandler implements EventHandler {

    @Autowired
    private AccountRepository accountRepository;
    @Override
    public void on(AccountOpenedEvent event) {
        var account = BankAccount.builder().accountHolder(event.getAccountHolder())
                .accountType(event.getAccountType())
                .balance(event.getOpeningBalance())
                .creationDate(event.getCreateedDate())
                .accountHolder(event.getAccountHolder())
                .build();
        accountRepository.save(account);
    }

    @Override
    public void on(AccountClosedEvent accountOpenedEvent) {
        var acc = accountRepository.findByAccountHolder(accountOpenedEvent.getIdentidier());
        if(acc.isEmpty())
            return;
        accountRepository.delete(acc.get());
    }

    @Override
    public void on(FundsWithDrawEvent event) {
        var acc = accountRepository.findByAccountHolder(event.getIdentidier());
        if(acc.isEmpty())
            return;
        var currentBalance = acc.get().getBalance();
        var latestBalance = currentBalance - event.getAmount();
        acc.get().setBalance(latestBalance);
        accountRepository.save(acc.get());

    }

    @Override
    public void on(FundsDepositedEvent event) {
        var acc = accountRepository.findByAccountHolder(event.getIdentidier());
        if(acc.isEmpty())
            return;
        var currentBalance = acc.get().getBalance();
        var latestBalance = currentBalance + event.getAmount();
        acc.get().setBalance(latestBalance);
        accountRepository.save(acc.get());
    }
}
