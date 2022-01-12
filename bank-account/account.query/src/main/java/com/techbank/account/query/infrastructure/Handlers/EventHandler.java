package com.techbank.account.query.infrastructure.Handlers;

import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithDrawEvent;


//Not to confuse the eventHandler(kafka..) on Query Side /// with the eventsourcinghandler resides on Command side and
// ultimatly impact the event store and read database
public interface EventHandler {
    void on(AccountOpenedEvent accountOpenedEvent);
    void on(AccountClosedEvent accountOpenedEvent);
    void on(FundsWithDrawEvent accountOpenedEvent);
    void on(FundsDepositedEvent accountOpenedEvent);
}
