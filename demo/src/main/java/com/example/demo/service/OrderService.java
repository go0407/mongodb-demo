package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.document.OrderLog;
import com.example.demo.entity.domain.Order;
import com.example.demo.mapper.OrderMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    private final MongoTemplate mongoTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper; // Spring Boot 自带的 Jackson

    private static final String TOPIC = "order-event-topic";

    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Order order) {
        // 1. 补全 MySQL 数据
        order.setOrderNo(UUID.randomUUID().toString());
        order.setCreateTime(LocalDateTime.now());
        order.setStatus("PENDING");

        // 2. 保存到 MySQL (MyBatis Plus)
        this.save(order);
        log.info("MySQL 保存成功: {}", order.getId());

        // 3. 记录日志到 MongoDB (存储复杂对象)
        // 将实体转为 Map 存入 Mongo
        Map<String, Object> map = objectMapper.convertValue(order, Map.class);

        OrderLog orderLog = OrderLog.builder()
                .orderNo(order.getOrderNo())
                .action("CREATE")
                .payload(map)
                .logTime(LocalDateTime.now())
                .build();

        OrderLog save = mongoTemplate.save(orderLog);
        log.info("MongoDB 日志保存成功 = {}", save);

        // 4. 发送 Kafka (使用配置好的 Jackson Serializer)
        kafkaTemplate.send(TOPIC, order.getOrderNo(), order);
        log.info("Kafka 消息发送成功");

        // 模拟报错回滚事务
        int i = 1 / 0;

        return order;
    }
}