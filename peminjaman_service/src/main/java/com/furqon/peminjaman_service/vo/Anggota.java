package com.furqon.peminjaman_service.vo;

public class Anggota {
    private Long id;
    private String nim;
    private String nama;
    private String email;
    private String jenisKelamin;

    public Anggota() {
    }

    public Anggota(Long id, String nim, String nama, String email,String jenisKelamin) {
        this.id = id;
        this.nim = nim;
        this.nama = nama;
        this.email = email;
        this.jenisKelamin = jenisKelamin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
