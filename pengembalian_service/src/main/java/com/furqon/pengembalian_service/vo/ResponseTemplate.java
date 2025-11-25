package com.furqon.pengembalian_service.vo;

import com.furqon.pengembalian_service.model.Pengembalian;

public class ResponseTemplate {
    Pengembalian pengembalian;
    Peminjaman peminjaman;
    Buku buku;
    Anggota anggota;

    public ResponseTemplate() {
    }

    public ResponseTemplate(Pengembalian pengembalian, Peminjaman peminjaman, Buku buku, Anggota anggota) {

        this.pengembalian = pengembalian;
        this.peminjaman = peminjaman;
        this.buku = buku;
        this.anggota = anggota;
    }

    public Pengembalian getPengembalian() {
        return pengembalian;
    }

    public void setPengembalian(Pengembalian pengembalian) {
        this.pengembalian = pengembalian;
    }

    public Peminjaman getPeminjaman() {
        return peminjaman;
    }

    public void setPeminjaman(Peminjaman peminjaman) {
        this.peminjaman = peminjaman;
    }

    public Buku getBuku() {
        return buku;
    }

    public void setBuku(Buku buku) {
        this.buku = buku;
    }

    public Anggota getAnggota() {
        return anggota;
    }

    public void setAnggota(Anggota anggota) {
        this.anggota = anggota;
    }
}
