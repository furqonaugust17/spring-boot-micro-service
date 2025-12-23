package com.furqon.peminjaman_service.dto;

import java.util.UUID;

import com.furqon.peminjaman_service.model.PeminjamanCommand;
import com.furqon.peminjaman_service.model.PeminjamanCommand.EventType;

public class PeminjamanEmailEvent {
    private UUID id;
    private PeminjamanCommand.EventType eventType;

    public PeminjamanEmailEvent(UUID id, EventType eventType) {
        this.id = id;
        this.eventType = eventType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PeminjamanCommand.EventType getEventType() {
        return eventType;
    }

    public void setEventType(PeminjamanCommand.EventType eventType) {
        this.eventType = eventType;
    }

}
