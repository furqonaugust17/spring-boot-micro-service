package com.furqon.pengembalian_service.vo;

import java.util.UUID;

public class Peminjaman {
    private UUID id;
    private String tanggalPinjam;
    private String tanggalKembali;
    private Long anggotaId;
    private Long bukuId;

    public Peminjaman() {
    }

    public Peminjaman(UUID id, String tanggalPinjam, String tanggalKembali, Long anggotaId, Long bukuId) {
        this.id = id;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
        this.anggotaId = anggotaId;
        this.bukuId = bukuId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTanggalPinjam() {
        return tanggalPinjam;
    }

    public void setTanggalPinjam(String tanggalPinjam) {
        this.tanggalPinjam = tanggalPinjam;
    }

    public String getTanggalKembali() {
        return tanggalKembali;
    }

    public void setTanggalKembali(String tanggalKembali) {
        this.tanggalKembali = tanggalKembali;
    }

    public Long getAnggotaId() {
        return anggotaId;
    }

    public void setAnggotaId(Long anggotaId) {
        this.anggotaId = anggotaId;
    }

    public Long getBukuId() {
        return bukuId;
    }

    public void setBukuId(Long bukuId) {
        this.bukuId = bukuId;
    }
}
