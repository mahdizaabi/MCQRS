package com.techbank.account.cmd.infrastructure;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.account.cmd.domain.EventStoreRepository;
import com.techbank.cqrs.core.events.BaseEvent;
import com.techbank.cqrs.core.events.EventModel;
import com.techbank.cqrs.core.exceptions.AggregateNotFoundException;
import com.techbank.cqrs.core.exceptions.ConcurrencyException;
import com.techbank.cqrs.core.infrastructure.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountEventStore implements EventStore {

    @Autowired
    private EventStoreRepository eventStoreRepo;
    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
    var eventStream = eventStoreRepo.findByAggregateIdentifier(aggregateId);

    //Concurrenct check: if two events are emitted concurrently(on the same time), both of them will hold the same expectedVersion
        //which is inappropriate for our eventstore.

    if(expectedVersion != -1 && eventStream.get(eventStream.size() -1).getVersion() != expectedVersion) {
        throw new ConcurrencyException();
    }
    for(BaseEvent event: events) {
           var eventModal = EventModel.builder()
                   .timeStamp(new Date())
                   .eventType(event.getClass().getTypeName())
                   .aggregateIdentifier(aggregateId)
                   .version(event.getVersion())
                   .aggregateType(AccountAggregate.class.getTypeName())
                   .eventData(event)
                   .build();
            var persistedEvent = eventStoreRepo.save(eventModal);
            if(persistedEvent != null) {
                //TODO: PRODUCE EVENT TO KAFKA.
            }
        }
    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        var eventStream = eventStoreRepo.findByAggregateIdentifier(aggregateId);
        if(eventStream == null || eventStream.isEmpty()) {
            throw new AggregateNotFoundException("Incorrect account id provided");
        }
        return eventStream.stream().map(EventModel::getEventData).collect(Collectors.toList());
    }
}
