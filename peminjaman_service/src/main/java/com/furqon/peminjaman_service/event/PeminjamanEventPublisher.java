package com.furqon.peminjaman_service.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import com.furqon.peminjaman_service.dto.PeminjamanEmailEvent;
import com.furqon.peminjaman_service.model.PeminjamanCommand;

@Component
public class PeminjamanEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key.transaction}")
    private String routingTransaction;

    @Value("${app.rabbitmq.routing-key.email}")
    private String routingEmail;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAfterCommit(PeminjamanCreatedEvent event) {

        PeminjamanCommand data = event.getPeminjaman();
        rabbitTemplate.convertAndSend(exchange, routingTransaction, data);
        rabbitTemplate.convertAndSend(exchange, routingEmail, new PeminjamanEmailEvent(
                data.getId(),
                data.getEventType()));
    }
}
