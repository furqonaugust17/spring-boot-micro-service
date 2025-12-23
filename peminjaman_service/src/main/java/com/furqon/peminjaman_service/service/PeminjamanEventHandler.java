package com.furqon.peminjaman_service.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.furqon.peminjaman_service.dto.PeminjamanEmailEvent;
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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key.email}")
    private String routingEmail;

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
        entity.setAnggotaId(event.getAnggotaId());
        entity.setBukuId(event.getBukuId());

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
        rabbitTemplate.convertAndSend(
                exchange,
                routingEmail, new PeminjamanEmailEvent(
                        event.getId(),
                        event.getEventType()));

        log.info("✅ MongoDB sync berhasil untuk ID {}", id);
    }
}
