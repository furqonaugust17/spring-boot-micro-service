package com.furqon.peminjaman_service.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
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

    @RabbitListener(queues = "${app.rabbitmq.queue.transaction}")
    @Transactional
    public void consume(PeminjamanCommand event) {
        String id = event.getId().toString();
        if (event == null || event.getId() == null) {
            log.warn("Menerima event null atau tanpa ID, pesan diabaikan.");
            return;
        }

        if (PeminjamanCommand.EventType.DELETED.equals(event.getEventType())) {
            peminjamanQueryRepository.deleteById(id);
            log.info("🗑️ Delete ke MongoDB berhasil untuk ID: {}", event.getId());
            return;
        }

        PeminjamanQuery entity = peminjamanQueryRepository.findById(id).orElse(new PeminjamanQuery());

        String anggotaUrl = "http://localhost:9002/api/anggota/" + event.getAnggotaId();
        Anggota anggota = restTemplate.getForObject(anggotaUrl, Anggota.class);

        String bukuUrl = "http://localhost:9002/api/buku/" + event.getAnggotaId();
        Buku buku = restTemplate.getForObject(bukuUrl, Buku.class);


        entity.setId(id);
        entity.setTanggalPinjam(event.getTanggalPinjam());
        entity.setTanggalKembali(event.getTanggalKembali());
        
        entity.setAnggotaId(event.getAnggotaId());
        if (anggota != null) {
            entity.setNama(anggota.getNama());
            entity.setEmail(anggota.getEmail());
            entity.setNim(anggota.getNim());
            entity.setJenisKelamin(anggota.getJenisKelamin());
        }

        entity.setBukuId(event.getBukuId());
        if (buku != null) {
            entity.setJudulBuku(buku.getJudul());
            entity.setPengarangBuku(buku.getPengarang());
            entity.setPenerbitBuku(buku.getPenerbit());
            entity.setTahunTerbitBuku(buku.getTahunTerbit());
        }

        peminjamanQueryRepository.save(entity);
        if (PeminjamanCommand.EventType.CREATED.equals(event.getEventType())) {
            log.info("📩 Insert ke MongoDB berhasil untuk ID: {}", entity.getId());
        } else {
            log.info("✅ Update ke MongoDB berhasil untuk ID: {}", entity.getId());
        }
    }
}
