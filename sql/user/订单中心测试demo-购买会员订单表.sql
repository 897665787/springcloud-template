DROP TABLE IF EXISTS `bu_member_buy_order`;
CREATE TABLE `bu_member_buy_order` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(11) NOT NULL COMMENT 'bu_user_info.id',
  `order_code` varchar(32) NOT NULL DEFAULT '' COMMENT '订单编号',
  `user_coupon_id` int(11) NOT NULL DEFAULT '0' COMMENT 'mk_user_coupon.id',
  `product_code` varchar(32) NOT NULL DEFAULT '' COMMENT '会员续期编码',
  `amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '会员续期费用(元)',
  `add_days` int(11) NOT NULL DEFAULT '0' COMMENT '续期增加天数',
  `refund_service_amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '退款手续费',
  
  `remark` varchar(255) DEFAULT NULL COMMENT '备注(多个使用/分隔)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_ordercode` (`order_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购买会员订单表';
