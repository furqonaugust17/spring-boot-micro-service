package com.furqon.pengembalian_service.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.furqon.pengembalian_service.model.Pengembalian;
import com.furqon.pengembalian_service.repository.PengembalianRepository;
import com.furqon.pengembalian_service.vo.Anggota;
import com.furqon.pengembalian_service.vo.Buku;
import com.furqon.pengembalian_service.vo.Peminjaman;
import com.furqon.pengembalian_service.vo.ResponseTemplate;

@Service
public class PengembalianService {
    @Autowired
    private PengembalianRepository pengembalianRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    public List<Pengembalian> getAllPengembalians() {
        return pengembalianRepository.findAll();
    }

    public Pengembalian getPengembalianById(Long id) {
        return pengembalianRepository.findById(id).orElse(null);
    }

    public Pengembalian createPengembalian(Pengembalian pengembalian) throws ParseException {

        long terlambat = this.calculateTerlambat(pengembalian);
        double denda = terlambat * 1000;

        pengembalian.setTerlambat(terlambat + " Hari");
        pengembalian.setDenda(denda);

        return pengembalianRepository.save(pengembalian);
    }

    public void deletePengembalian(Long id) {
        pengembalianRepository.deleteById(id);
    }

    public List<ResponseTemplate> getPengembalianWithPeminjamanById(Long id) {
        List<ResponseTemplate> responseTemplates = new ArrayList<>();
        Pengembalian pengembalian = getPengembalianById(id);

        if (pengembalian == null) {
            return null;
        }

        ServiceInstance serviceInstance = discoveryClient.getInstances("API-GATEWAY-PUSTAKA").get(0);

        Peminjaman peminjaman = restTemplate.getForObject(
                serviceInstance.getUri() + "/api/peminjaman/" + pengembalian.getPeminjamanId(), Peminjaman.class);
        Anggota anggota = restTemplate
                .getForObject(serviceInstance.getUri() + "/api/anggota/" + peminjaman.getAnggotaId(), Anggota.class);
        Buku buku = restTemplate.getForObject(serviceInstance.getUri() + "/api/buku/" + peminjaman.getBukuId(),
                Buku.class);

        ResponseTemplate vo = new ResponseTemplate();
        vo.setPengembalian(pengembalian);
        vo.setPeminjaman(peminjaman);
        vo.setBuku(buku);
        vo.setAnggota(anggota);

        responseTemplates.add(vo);

        return responseTemplates;
    }

    public Peminjaman getPeminjaman(Long id) {
        try {
            ServiceInstance serviceInstance = discoveryClient.getInstances("API-GATEWAY-PUSTAKA").get(0);
            Peminjaman peminjaman = restTemplate.getForObject(serviceInstance.getUri() + "/api/peminjaman/" + id,
                    Peminjaman.class);
            return peminjaman;
        } catch (Exception e) {
            return null;
        }
    }

    public long calculateTerlambat(Pengembalian pengembalian) throws ParseException {
        Peminjaman peminjaman = this.getPeminjaman(pengembalian.getPeminjamanId());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Date tanggalKembali = sdf.parse(peminjaman.getTanggalKembali());
        Date tanggalDikembalikan = sdf.parse(pengembalian.getTanggalDikembalikan());

        long diffInMillies = tanggalDikembalikan.getTime() - tanggalKembali.getTime();
        long jumlahHari = diffInMillies < 0 ? 0 : Math.abs(diffInMillies);
        long terlambat = TimeUnit.DAYS.convert(jumlahHari, TimeUnit.MILLISECONDS);
        return terlambat;
    }
}
