package com.furqon.peminjaman_service.service;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.furqon.peminjaman_service.event.PeminjamanCreatedEvent;
import com.furqon.peminjaman_service.model.PeminjamanCommand;
import com.furqon.peminjaman_service.repository.jpa.PeminjamanCommandRepository;

import jakarta.transaction.Transactional;

@Service
public class PeminjamanCommandService {
    @Autowired
    private PeminjamanCommandRepository peminjamanCommandRepository;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public PeminjamanCommand createPeminjaman(PeminjamanCommand peminjaman) {

        ServiceInstance serviceInstance = discoveryClient
                .getInstances("API-GATEWAY-PUSTAKA")
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("API Gateway tidak tersedia"));

        String baseUrl = serviceInstance.getUri().toString();

        try {
            RestTemplate restTemplate = new RestTemplate();

            restTemplate.getForObject(
                    baseUrl + "/api/anggota/" + peminjaman.getAnggotaId(),
                    Void.class);

            restTemplate.getForObject(
                    baseUrl + "/api/buku/" + peminjaman.getBukuId(),
                    Void.class);

            PeminjamanCommand saved = peminjamanCommandRepository.save(peminjaman);
            saved.setEventType(PeminjamanCommand.EventType.CREATED);

            eventPublisher.publishEvent(new PeminjamanCreatedEvent(saved));

            return saved;

        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Anggota atau Buku tidak ditemukan");

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Gagal membuat peminjaman");
        }
    }

    @Transactional
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
        eventPublisher.publishEvent(new PeminjamanCreatedEvent(updated));
        return peminjamanCommand;
    }

    @Transactional
    public void deletePeminjaman(UUID id) {
        if (!peminjamanCommandRepository.existsById(id)) {
            throw new RuntimeException("Peminjaman dengan id " + id + " tidak ditemukan");
        }

        peminjamanCommandRepository.deleteById(id);

        PeminjamanCommand peminjamanEvent = new PeminjamanCommand();
        peminjamanEvent.setId(id);
        peminjamanEvent.setEventType(PeminjamanCommand.EventType.DELETED);

        eventPublisher.publishEvent(new PeminjamanCreatedEvent(peminjamanEvent));
    }
}
