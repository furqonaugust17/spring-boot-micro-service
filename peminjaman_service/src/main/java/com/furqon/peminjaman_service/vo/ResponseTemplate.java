package com.furqon.peminjaman_service.vo;

import com.furqon.peminjaman_service.model.Peminjaman;

public class ResponseTemplate {
    Peminjaman peminjaman;
    Anggota anggota;
    Buku buku;

    public ResponseTemplate() {
    }

    public ResponseTemplate(Anggota anggota, Buku buku, Peminjaman peminjaman) {
        this.anggota = anggota;
        this.buku = buku;
        this.peminjaman = peminjaman;
    }

    public Anggota getAnggota() {
        return anggota;
    }

    public void setAnggota(Anggota anggota) {
        this.anggota = anggota;
    }

    public Buku getBuku() {
        return buku;
    }

    public void setBuku(Buku buku) {
        this.buku = buku;
    }

    public Peminjaman getPeminjaman() {
        return peminjaman;
    }

    public void setPeminjaman(Peminjaman peminjaman) {
        this.peminjaman = peminjaman;
    }
}
