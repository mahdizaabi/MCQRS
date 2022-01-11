package com.techbank.cqrs.core.domain;

import com.techbank.cqrs.core.events.BaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot {
    protected String id;
    private int version = -1;
    private final List<BaseEvent> change = new ArrayList<>();
    private final static Logger LOGGER = LoggerFactory.getLogger(AggregateRoot.class);

    public String getId() {
        return this.id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<BaseEvent> getUncommitedChange() {
        return this.change;
    }

    public void markChangesAsCommited() {
        this.change.clear();
    }

    protected void applyChange(BaseEvent event, Boolean isNewEvent) {
        try {
            var method = getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (NoSuchMethodException exc) {
            exc.printStackTrace();
            LOGGER.error("The apply method was not found");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error applying event to aggregate {}", e.getMessage());

        } finally {
            if (isNewEvent)
                change.add(event);
        }
    }

    public void raiseEvent(BaseEvent event) {
        applyChange(event, true);
    }

    public void replayEvents(Iterable<BaseEvent> events) {
        events.forEach(event -> applyChange(event, false));
    }
}
