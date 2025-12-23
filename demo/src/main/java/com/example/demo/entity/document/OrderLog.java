package com.example.demo.entity.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@Document(collection = "order_logs")
public class OrderLog {
    @Id
    private String id;
    private String orderNo;
    private String action; // "CREATE", "UPDATE"
    private Map<String, Object> payload; // 存储完整JSON数据
    private LocalDateTime logTime;
}