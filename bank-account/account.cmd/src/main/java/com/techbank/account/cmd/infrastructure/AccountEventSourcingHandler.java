package com.techbank.account.cmd.infrastructure;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.cqrs.core.Handlers.EventSourcingHandler;
import com.techbank.cqrs.core.domain.AggregateRoot;
import com.techbank.cqrs.core.events.BaseEvent;
import com.techbank.cqrs.core.infrastructure.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {
    @Autowired
    private EventStore eventStore;


    @Override
    public void save(AggregateRoot aggregate) {
        eventStore.saveEvents(aggregate.getId(), aggregate.getUncommitedChange(), aggregate.getVersion());

        aggregate.markChangesAsCommited();
    }

    /*
    Recreate the latest state of the aggregate
     */
    @Override
    public AccountAggregate getById(String aggregateId) {

        var aggregate = new AccountAggregate();
        var events = eventStore.getEvents(aggregateId);
        if (events != null && !events.isEmpty()) {
            aggregate.replayEvents(events);
            var latestVersion = events.stream()
                    .map(x -> x.getVersion()).max(Comparator.naturalOrder());
            aggregate.setVersion(latestVersion.get());
        }


        return aggregate;
    }
}
