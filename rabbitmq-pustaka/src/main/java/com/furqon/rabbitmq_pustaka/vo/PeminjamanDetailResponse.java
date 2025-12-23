package com.furqon.rabbitmq_pustaka.vo;

import java.util.UUID;

public class PeminjamanDetailResponse {

    private UUID id;
    private String tanggalPinjam;
    private String tanggalKembali;

    private Long anggotaId;
    private String nim;
    private String nama;
    private String email;
    private String jenisKelamin;

    private Long bukuId;
    private String judulBuku;
    private String pengarangBuku;
    private String penerbitBuku;
    private String tahunTerbitBuku;

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

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public Long getBukuId() {
        return bukuId;
    }

    public void setBukuId(Long bukuId) {
        this.bukuId = bukuId;
    }

    public String getJudulBuku() {
        return judulBuku;
    }

    public void setJudulBuku(String judulBuku) {
        this.judulBuku = judulBuku;
    }

    public String getPengarangBuku() {
        return pengarangBuku;
    }

    public void setPengarangBuku(String pengarangBuku) {
        this.pengarangBuku = pengarangBuku;
    }

    public String getPenerbitBuku() {
        return penerbitBuku;
    }

    public void setPenerbitBuku(String penerbitBuku) {
        this.penerbitBuku = penerbitBuku;
    }

    public String getTahunTerbitBuku() {
        return tahunTerbitBuku;
    }

    public void setTahunTerbitBuku(String tahunTerbitBuku) {
        this.tahunTerbitBuku = tahunTerbitBuku;
    }
}
