package com.furqon.rabbitmq_pustaka.dto;

import java.util.UUID;

import com.furqon.rabbitmq_pustaka.event.EventType;

public class PeminjamanEventPayload {
    private UUID id;
    private EventType eventType;

    public PeminjamanEventPayload(UUID id, EventType eventType) {
        this.id = id;
        this.eventType = eventType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

}