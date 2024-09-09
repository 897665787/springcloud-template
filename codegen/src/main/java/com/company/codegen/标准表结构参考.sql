CREATE TABLE `pay_refund_apply` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_code` varchar(32) NOT NULL DEFAULT '' COMMENT '退款订单号',
  `old_order_code` varchar(32) NOT NULL DEFAULT '' COMMENT '原订单号',
  -- 自动生成的代码会自动去掉(...)的内容
  `amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '退款金额(元)',
  -- 使用        (),:    来标准化字段，会自动生成option选项
  `business_type` varchar(16) NOT NULL DEFAULT '' COMMENT '业务类型(USER:用户申请,SYS_AUTO:系统自动退款,AMDIN:管理后台申请)',
  -- 使用        (),:    来标准化字段，会自动生成option选项
  `verify_status` int(11) NOT NULL DEFAULT '0' COMMENT '审核状态(1:待审核,21:通过,22:驳回)',
  -- 使用        (),:    来标准化字段，会自动生成option选项
  `refund_status` int(11) NOT NULL DEFAULT '0' COMMENT '退款状态(1:未退款,21:退款驳回(END),31:处理中,41:申请成功,42:申请失败(END),51:退款成功(END),52:退款失败(END))',
  `reason` varchar(64) NOT NULL DEFAULT '' COMMENT '退款原因',
  `attach` varchar(255) NOT NULL DEFAULT '' COMMENT '通知地址的附加数据',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` int(11) unsigned NOT NULL DEFAULT 0 COMMENT '创建人', -- 管理后台生成的数据需要该字段
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` int(11) unsigned NOT NULL DEFAULT 0 COMMENT '更新人', -- 管理后台生成的数据需要该字段
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_ordercode` (`order_code`),
  KEY `idx_rs_vs` (`refund_status`,`verify_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退款申请表';-- 自动生成的代码会自动去掉最后1个表字
