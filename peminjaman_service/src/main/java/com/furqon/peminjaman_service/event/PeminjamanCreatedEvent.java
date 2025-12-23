package com.furqon.peminjaman_service.event;

import com.furqon.peminjaman_service.model.PeminjamanCommand;

public class PeminjamanCreatedEvent {

    private final PeminjamanCommand peminjaman;

    public PeminjamanCreatedEvent(PeminjamanCommand peminjaman) {
        this.peminjaman = peminjaman;
    }

    public PeminjamanCommand getPeminjaman() {
        return peminjaman;
    }
}
