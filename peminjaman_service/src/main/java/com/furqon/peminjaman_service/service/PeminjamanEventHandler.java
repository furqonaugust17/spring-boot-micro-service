package com.furqon.peminjaman_service.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.furqon.peminjaman_service.model.PeminjamanCommand;
import com.furqon.peminjaman_service.model.PeminjamanQuery;
import com.furqon.peminjaman_service.repository.mongo.PeminjamanQueryRepository;
import com.furqon.peminjaman_service.vo.Anggota;
import com.furqon.peminjaman_service.vo.Buku;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeminjamanEventHandler {
    private final PeminjamanQueryRepository peminjamanQueryRepository;
    private final RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @RabbitListener(queues = "${app.rabbitmq.queue.transaction}")
    @Transactional
    public void consume(PeminjamanCommand event) {

        if (event == null || event.getId() == null) {
            log.warn("Event null, diabaikan");
            return;
        }

        String id = event.getId().toString();

        if (PeminjamanCommand.EventType.DELETED.equals(event.getEventType())) {
            peminjamanQueryRepository.deleteById(id);
            log.info("🗑️ Delete Mongo ID {}", id);
            return;
        }

        PeminjamanQuery entity = peminjamanQueryRepository
                .findById(id)
                .orElse(new PeminjamanQuery());

        ServiceInstance serviceInstance = discoveryClient
                .getInstances("API-GATEWAY-PUSTAKA")
                .stream()
                .findFirst()
                .orElseThrow();

        String baseUrl = serviceInstance.getUri().toString();

        Anggota anggota = restTemplate.getForObject(
                baseUrl + "/api/anggota/" + event.getAnggotaId(),
                Anggota.class);

        Buku buku = restTemplate.getForObject(
                baseUrl + "/api/buku/" + event.getBukuId(),
                Buku.class);

        entity.setId(id);
        entity.setTanggalPinjam(event.getTanggalPinjam());
        entity.setTanggalKembali(event.getTanggalKembali());

        if (anggota != null) {
            entity.setNama(anggota.getNama());
            entity.setEmail(anggota.getEmail());
            entity.setNim(anggota.getNim());
            entity.setJenisKelamin(anggota.getJenisKelamin());
        }

        if (buku != null) {
            entity.setJudulBuku(buku.getJudul());
            entity.setPengarangBuku(buku.getPengarang());
            entity.setPenerbitBuku(buku.getPenerbit());
            entity.setTahunTerbitBuku(buku.getTahunTerbit());
        }

        peminjamanQueryRepository.save(entity);

        log.info("✅ MongoDB sync berhasil untuk ID {}", id);
    }
}
