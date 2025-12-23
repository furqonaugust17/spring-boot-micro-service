package com.furqon.rabbitmq_pustaka;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Payload;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.furqon.rabbitmq_pustaka.dto.PeminjamanEventPayload;
import com.furqon.rabbitmq_pustaka.event.EventType;
import com.furqon.rabbitmq_pustaka.notification.PeminjamanEmailTemplate;
import com.furqon.rabbitmq_pustaka.vo.PeminjamanDetailResponse;

@Service
public class PeminjamanConsumerService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.mail.from}")
    private String from;

    @RabbitListener(queues = "${app.rabbitmq-peminjaman.queue}")

    @Transactional
    public void receiveOrder(PeminjamanEventPayload payload) {

        if (payload == null || payload.getEventType() == null || payload.getId() == null) {
            return;
        }

        if (payload.getEventType() == EventType.DELETED) {
            System.out.println("Delete event diterima, email tidak dikirim. ID: " + payload.getId());
            return;
        }

        System.out.println("Receive email event: " + payload.getEventType()
                + " | ID: " + payload.getId());

        try {
            ServiceInstance serviceInstance = discoveryClient
                    .getInstances("API-GATEWAY-PUSTAKA")
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("API Gateway tidak tersedia"));

            String url = serviceInstance.getUri()
                    + "/api/peminjaman/" + payload.getId();

            PeminjamanDetailResponse data = restTemplate.getForObject(
                    url,
                    PeminjamanDetailResponse.class);

            if (data == null || data.getEmail() == null) {
                throw new RuntimeException("Data peminjaman atau email kosong");
            }

            String subject;
            String messageBody;

            switch (payload.getEventType()) {
                case CREATED:
                    subject = "Konfirmasi Peminjaman Buku";
                    messageBody = PeminjamanEmailTemplate.buildCreateMessage(data);
                    break;

                case UPDATED:
                    subject = "Perubahan Data Peminjaman Buku";
                    messageBody = PeminjamanEmailTemplate.buildUpdateMessage(data);
                    break;

                default:
                    return;
            }
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(data.getEmail());
            mailMessage.setSubject(subject);
            mailMessage.setText(messageBody);

            javaMailSender.send(mailMessage);

            System.out.println("Email berhasil dikirim ke: " + data.getEmail());

        } catch (Exception e) {
            System.out.println("Gagal mengirim email peminjaman");
            e.printStackTrace();
        }
    }

}