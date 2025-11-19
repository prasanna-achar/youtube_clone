package com.nts.upload.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.queue.video.process.name}")
    private String videoProcessQueueName;

    @Value("${rabbitmq.queue.video.upload.name}")
    private String videoUploadQueueName;



    @Value("${rabbitmq.queue.video.process.key}")
    private String videoProcessRoutingKey;

    @Value("${rabbitmq.queue.video.upload.key}")
    private String videoUploadRoutingKey;



    // Exchange
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchangeName, true, false);
    }

    // Queues
    @Bean
    public Queue videoProcessingQueue() {
        return new Queue(videoProcessQueueName, true);
    }

    @Bean
    public Queue videoUploadingQueue() {
        return new Queue(videoUploadQueueName, true);
    }



    // Bindings
    @Bean
    public Binding videoProcessingBinding(Queue videoProcessingQueue, TopicExchange exchange) {
        return BindingBuilder.bind(videoProcessingQueue).to(exchange).with(videoProcessRoutingKey);
    }

    @Bean
    public Binding videoUploadingBinding(Queue videoUploadingQueue, TopicExchange exchange) {
        return BindingBuilder.bind(videoUploadingQueue).to(exchange).with(videoUploadRoutingKey);
    }



    // JSON Converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    // RabbitAdmin - ensures queues/exchange/bindings exist
    @Bean
    @DependsOn({"videoProcessingQueue", "videoUploadingQueue",
            "videoProcessingBinding", "videoUploadingBinding"})
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }
}
