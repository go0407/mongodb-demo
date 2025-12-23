CREATE TABLE `t_order` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `order_no` varchar(64) DEFAULT NULL,
                           `amount` decimal(10,2) DEFAULT NULL,
                           `status` varchar(20) DEFAULT NULL,
                           `create_time` datetime DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;