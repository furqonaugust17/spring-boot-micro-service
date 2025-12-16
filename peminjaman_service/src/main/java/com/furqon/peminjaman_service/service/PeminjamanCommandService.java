package com.furqon.peminjaman_service.service;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.furqon.peminjaman_service.model.PeminjamanCommand;
import com.furqon.peminjaman_service.repository.jpa.PeminjamanCommandRepository;
import com.furqon.peminjaman_service.vo.Anggota;
import com.furqon.peminjaman_service.vo.Buku;

@Service
public class PeminjamanCommandService {
    @Autowired
    private PeminjamanCommandRepository peminjamanCommandRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.queue.transaction}")
    private String queueTransaction;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key.transaction}")
    private String routingKey;

    @Autowired
    private DiscoveryClient discoveryClient;

    public PeminjamanCommand createPeminjaman(PeminjamanCommand peminjaman) {
        ServiceInstance serviceInstance = discoveryClient.getInstances("API-GATEWAY-PUSTAKA").get(0);

        RestTemplate restTemplate = new RestTemplate();
        String anggotaUrl = serviceInstance.getUri() + "/api/anggota/" + peminjaman.getAnggotaId();
        Anggota anggota = restTemplate.getForObject(anggotaUrl, Anggota.class);

        String bukuUrl = serviceInstance.getUri() + "/api/buku/" + peminjaman.getBukuId();
        Buku buku = restTemplate.getForObject(bukuUrl, Buku.class);

        if (anggota == null || buku == null) {
            return null;
        }

        PeminjamanCommand saved = peminjamanCommandRepository.save(peminjaman);

        saved.setEventType(PeminjamanCommand.EventType.CREATED);
        rabbitTemplate.convertAndSend(exchange, routingKey, saved);

        return saved;
    }

    public PeminjamanCommand updatePeminjaman(UUID id, PeminjamanCommand peminjaman) {
        PeminjamanCommand peminjamanCommand = peminjamanCommandRepository.findById(id)
                .orElse(null);

        if (peminjamanCommand == null) {
            return null;
        }

        peminjamanCommand.setTanggalPinjam(peminjaman.getTanggalPinjam());
        peminjamanCommand.setTanggalKembali(peminjaman.getTanggalKembali());
        peminjamanCommand.setBukuId(peminjaman.getBukuId());
        peminjamanCommand.setAnggotaId(peminjaman.getAnggotaId());

        PeminjamanCommand updated = peminjamanCommandRepository.save(peminjamanCommand);

        updated.setEventType(PeminjamanCommand.EventType.UPDATED);
        rabbitTemplate.convertAndSend(exchange, routingKey, updated);
        return peminjamanCommand;
    }

    public void deletePeminjaman(UUID id) {
        if (!peminjamanCommandRepository.existsById(id)) {
            throw new RuntimeException("Peminjaman dengan id " + id + " tidak ditemukan");
        }

        peminjamanCommandRepository.deleteById(id);

        PeminjamanCommand peminjamanEvent = new PeminjamanCommand();
        peminjamanEvent.setId(id);
        peminjamanEvent.setEventType(PeminjamanCommand.EventType.DELETED);

        rabbitTemplate.convertAndSend(exchange, routingKey, peminjamanEvent);
    }
}
