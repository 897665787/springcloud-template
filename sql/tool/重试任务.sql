CREATE TABLE `retry_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `url` varchar(128) NOT NULL COMMENT '回调地址',
  `json_params` varchar(2048) DEFAULT NULL COMMENT 'JSON参数',
  `json_result` varchar(128) DEFAULT NULL COMMENT 'JSON结果',
  `status` tinyint(4) NOT NULL COMMENT '状态(1:待回调,2:回调成功(END),21.回调失败,22.放弃回调(END))',
  `seconds_strategy` varchar(32) NOT NULL DEFAULT 'increment' COMMENT '递增秒数策略(increment:增量,fix:固定)',
  `increase_seconds` int(11) NOT NULL DEFAULT '2' COMMENT '递增秒数',
  `next_dispose_time` datetime NOT NULL COMMENT '下次处理时间',
  `max_failure` int(11) NOT NULL COMMENT '最大允许失败次数',
  `failure` int(11) NOT NULL DEFAULT '0' COMMENT '当前失败次数',
  
  `trace_id` varchar(32) DEFAULT NULL COMMENT '追踪ID',
  
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status_ndt` (`status`,`next_dispose_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='重试任务';