package com.furqon.peminjaman_service;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.queue.email}")
    private String queueEmail;

    @Value("${app.rabbitmq.queue.transaction}")
    private String queueTransaction;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key.transaction}")
    private String routingTransaction;

    @Value("${app.rabbitmq.routing-key.email}")
    private String routingEmail;

    @Bean
    public Queue emailQueue() {
        return new Queue(queueEmail, true, false, false);
    }

    @Bean
    public Queue transactionQueue() {
        return new Queue(queueTransaction, true, false, false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding transactionBinding(Queue transactionQueue, DirectExchange exchange) {
        return BindingBuilder.bind(transactionQueue)
        .to(exchange)
        .with(routingTransaction);
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, DirectExchange exchange) {
        return BindingBuilder.bind(emailQueue)
                .to(exchange)
                .with(routingEmail);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}