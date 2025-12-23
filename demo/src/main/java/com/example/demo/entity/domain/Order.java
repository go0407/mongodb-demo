package com.example.demo.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_order")
public class Order implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createTime;
}