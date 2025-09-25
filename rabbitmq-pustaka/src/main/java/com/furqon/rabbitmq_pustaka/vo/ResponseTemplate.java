package com.furqon.rabbitmq_pustaka.vo;

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

   public String sendMailMessage() {
    StringBuilder message = new StringBuilder();

    message.append("Subject: Konfirmasi Peminjaman Buku Berhasil\n\n");

    message.append("Yth. ").append(anggota.getNama()).append(",\n\n");
    message.append("Terima kasih telah melakukan peminjaman buku di perpustakaan kami.\n");
    message.append("Berikut adalah rincian lengkap peminjaman Anda:\n\n");

    message.append("------------------------------------------\n");
    message.append("DETAIL PEMINJAMAN\n");
    message.append("------------------------------------------\n");
    message.append("ID Peminjaman  : ").append(peminjaman.getId()).append("\n");
    message.append("Nama Anggota   : ").append(anggota.getNama()).append("\n\n");
    message.append("NIM Anggota    : ").append(anggota.getNim()).append("\n\n");
    message.append("Jenis Kelamin  : ").append(anggota.getJenisKelamin()).append("\n\n");

    message.append("Judul Buku     : ").append(buku.getJudul()).append("\n");
    message.append("Pengarang      : ").append(buku.getPengarang()).append("\n\n");

    message.append("Tanggal Pinjam : ").append(peminjaman.getTanggalPinjam()).append("\n");
    message.append("Tgl. Kembali   : ").append(peminjaman.getTanggalKembali()).append("\n");
    message.append("------------------------------------------\n\n");

    message.append("Mohon untuk mengembalikan buku tepat waktu sebelum tanggal jatuh tempo untuk menghindari denda.\n\n");
    message.append("Hormat kami,\n");
    message.append("Tim Perpustakaan Digital\n");

    return message.toString();
}
}
