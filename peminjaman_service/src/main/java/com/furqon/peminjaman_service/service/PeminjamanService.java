package com.furqon.peminjaman_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.furqon.peminjaman_service.model.Peminjaman;
import com.furqon.peminjaman_service.repository.PeminjamanRepository;
import com.furqon.peminjaman_service.vo.Anggota;
import com.furqon.peminjaman_service.vo.Buku;
import com.furqon.peminjaman_service.vo.ResponseTemplate;

@Service
public class PeminjamanService {
    
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${app.rabbitmq.exchange}")
    private String exchange;
    
    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    @Autowired
    private PeminjamanRepository peminjamanRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    public PeminjamanService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Peminjaman> getAllPeminjaman(){
        return peminjamanRepository.findAll();
    }

     public Peminjaman getPeminjamanById(Long id) {
        return peminjamanRepository.findById(id).orElse(null);
    }

    public Peminjaman createPeminjaman(Peminjaman peminjaman) {
        Peminjaman savedPeminjamanan = peminjamanRepository.save(peminjaman);

        rabbitTemplate.convertAndSend(exchange, routingKey, savedPeminjamanan);
        return savedPeminjamanan;
    }

    public void deletePeminjaman(Long id) {
        peminjamanRepository.deleteById(id);
    }

    public List<ResponseTemplate> getPeminjamanWithAnggotaBukuById(Long id){
        List<ResponseTemplate> responseTemplates = new ArrayList<>();
        Peminjaman peminjaman = getPeminjamanById(id);

        if(peminjaman == null){
            return null;
        }

        ServiceInstance serviceInstance = discoveryClient.getInstances("API-GATEWAY-PUSTAKA").get(0);

        Anggota anggota = restTemplate.getForObject(serviceInstance.getUri() + "/api/anggota/" + peminjaman.getAnggotaId(), Anggota.class);
        Buku buku = restTemplate.getForObject(serviceInstance.getUri() + "/api/buku/" + peminjaman.getBukuId(), Buku.class);

        ResponseTemplate vo = new ResponseTemplate();
        vo.setPeminjaman(peminjaman);
        vo.setAnggota(anggota);
        vo.setBuku(buku);

        responseTemplates.add(vo);

        return responseTemplates;
    }
}
