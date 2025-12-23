package com.furqon.rabbitmq_pustaka.notification;

import com.furqon.rabbitmq_pustaka.vo.PeminjamanDetailResponse;

public class PeminjamanEmailTemplate {
    public static String buildCreateMessage(PeminjamanDetailResponse data) {

        return """
                Yth. %s,

                Peminjaman buku Anda BERHASIL dibuat.

                DETAIL PEMINJAMAN
                ----------------------------------
                ID Peminjaman  : %s
                Nama           : %s
                NIM            : %s
                Buku           : %s
                Tanggal Pinjam : %s
                Tgl. Kembali   : %s

                Mohon mengembalikan buku tepat waktu.

                Hormat kami,
                Tim Perpustakaan Digital
                """.formatted(
                data.getNama(),
                data.getId(),
                data.getNama(),
                data.getNim(),
                data.getJudulBuku(),
                data.getTanggalPinjam(),
                data.getTanggalKembali());
    }

    public static String buildUpdateMessage(PeminjamanDetailResponse data) {

        return """
                Yth. %s,

                Data peminjaman buku Anda TELAH DIPERBARUI.

                DETAIL PEMINJAMAN TERBARU
                ----------------------------------
                ID Peminjaman  : %s
                Buku           : %s
                Tanggal Pinjam : %s
                Tgl. Kembali   : %s

                Silakan periksa kembali detail peminjaman Anda.

                Hormat kami,
                Tim Perpustakaan Digital
                """.formatted(
                data.getNama(),
                data.getId(),
                data.getNama(),
                data.getNim(),
                data.getJudulBuku(),
                data.getTanggalPinjam(),
                data.getTanggalKembali());
    }
}
