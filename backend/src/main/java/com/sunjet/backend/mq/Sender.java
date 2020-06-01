package com.sunjet.backend.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class Sender implements RabbitTemplate.ConfirmCallback {
//public class Sender {


    @Autowired
    private RabbitTemplate rabbitTemplate;

//    @PostConstruct
//    public void init() {
//        rabbitTemplate.setConfirmCallback(this);
//        rabbitTemplate.setReturnCallback(this);
//    }

    public void send(String message, String exchange, String routingKey) throws InterruptedException {
//        while (true) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        log.info("开始发送消息[延迟10秒] : " + message.toLowerCase());
        MessagePostProcessor processor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration(10000 + "");
                return message;
            }
        };
        rabbitTemplate.convertAndSend(exchange, routingKey, message, processor, correlationId);


//        rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationId);
//        rabbitTemplate.convertAndSend();
//            String response = rabbitTemplate.convertSendAndReceive("topicExchange", "key.1", message, correlationId).toString();
        System.out.println("发送队列 : " + routingKey);
        System.out.println("结束发送消息 : " + message.toLowerCase());
//            System.out.println("消费者响应 : " + response + " 消息处理完成");
//            Thread.sleep(2000);
//        }
//        log.info(String.format("发送消息: %s", message));


    }

    //
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            System.out.println("消息发送成功:" + correlationData);
        } else {
            System.out.println("消息发送失败:" + cause);
        }
    }
//
//    @Override
//    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
//        System.out.println(message.getMessageProperties().getCorrelationIdString() + " 发送失败");
//    }
}