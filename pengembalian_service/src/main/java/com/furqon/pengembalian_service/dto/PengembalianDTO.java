package com.furqon.pengembalian_service.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PengembalianDTO {
    private String tanggalKembali;
    private String tanggalDikembalikan;
    private Long terlambat;
    private double denda = 1000;

    public PengembalianDTO(String tanggalKembali, String tanggalDikembalikan) throws ParseException {
        this.tanggalKembali = tanggalKembali;
        this.tanggalDikembalikan = tanggalDikembalikan;
        this.setTerlambat(tanggalKembali, tanggalDikembalikan);
        this.setDenda();
    }

    public String getTanggalDikembalikan() {
        return tanggalDikembalikan;
    }

    public void setTanggalDikembalikan(String tanggalDikembalikan) {
        this.tanggalDikembalikan = tanggalDikembalikan;
    }

    public Long getTerlambat() {
        return terlambat;
    }

    public void setTerlambat(String tanggalKembali, String tanggalDikembalikan) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Date tglKembali = sdf.parse(tanggalKembali);
        Date tglDikembalikan = sdf.parse(tanggalDikembalikan);

        long diffInMillies = tglDikembalikan.getTime() - tglKembali.getTime();
        long jumlahHari = diffInMillies < 0 ? 0 : Math.abs(diffInMillies);
        long terlambat = TimeUnit.DAYS.convert(jumlahHari, TimeUnit.MILLISECONDS);
        this.terlambat = terlambat;
    }

    public String getTanggalKembali() {
        return tanggalKembali;
    }

    public void setTanggalKembali(String tanggalKembali) {
        this.tanggalKembali = tanggalKembali;
    }

    public double getDenda() {
        return denda;
    }

    public void setDenda() {
        this.denda = this.terlambat * this.denda;
    }
}
