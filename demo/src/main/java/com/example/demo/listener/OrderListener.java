package com.example.demo.listener;

import com.example.demo.entity.domain.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderListener {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-event-topic", groupId = "order-group")
    public void onMessage(ConsumerRecord<String, Order> record) {
        // Spring Kafka 会根据配置自动将 Value 反序列化为 Order 对象
        Order order = record.value();
        
        log.info("Kafka 收到对象 - 订单号: {}, 金额: {}", order.getOrderNo(), order.getAmount());

        try {
            // 演示：使用 Jackson 手动序列化打印日志
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter()
                                           .writeValueAsString(order);
            log.info("Jackson 序列化结果:\n{}", jsonString);
        } catch (JsonProcessingException e) {
            log.error("JSON处理异常", e);
        }
    }
}