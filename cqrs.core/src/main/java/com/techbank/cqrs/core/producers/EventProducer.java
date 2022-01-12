package com.techbank.cqrs.core.producers;

import com.techbank.cqrs.core.events.BaseEvent;


//Kafka events
public interface EventProducer {
    void produceEvent(String key, BaseEvent baseEvent);
}
