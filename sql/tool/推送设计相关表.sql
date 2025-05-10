CREATE TABLE `push_device_bind` (
 `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',

 `deviceid` varchar(64) NOT NULL COMMENT '设备ID',
 `push_id` varchar(64) NOT NULL COMMENT '推送ID（推送平台唯一）',
 `device_type` varchar(16) NOT NULL COMMENT '设备类型(Android:安卓,iOS:苹果)',

 `remark` varchar(255) DEFAULT NULL COMMENT '备注',
 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
 PRIMARY KEY (`id`) USING BTREE,
 UNIQUE KEY `uniq_deviceid` (`deviceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推送设备绑定';

CREATE TABLE `push_template` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type` varchar(16) NOT NULL COMMENT '业务类型(verifycode:验证码,market:营销活动,tips:提示信息)',
  `template_title` varchar(32) DEFAULT NULL COMMENT '模板标题',
  `template_content` varchar(255) DEFAULT NULL COMMENT '模板内容',
  `template_intent` varchar(255) DEFAULT NULL COMMENT '模板意图',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推送模板';

CREATE TABLE `push_task` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type` varchar(16) NOT NULL COMMENT '业务类型(verifycode:验证码,market:营销活动,tips:提示信息)',
  `plan_send_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '计划发送时间',
  `over_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '超时取消发送时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推送发送任务';

CREATE TABLE `push_task_detail` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `task_id` int(11) unsigned NOT NULL COMMENT 'push_task.id',
  `deviceid` varchar(64) NOT NULL COMMENT '设备ID',
  `template_param_json` varchar(255) DEFAULT NULL COMMENT '模板参数json',
  `plan_send_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '计划发送时间',
  `actual_send_time` datetime DEFAULT NULL COMMENT '实际发送时间',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态(1:待发送,2:待定时发送,11:发送MQ成功,12:MQ消费成功,21:请求成功,22:请求失败,31:取消发送)',
  `title` varchar(32) DEFAULT NULL COMMENT '推送标题',
  `content` varchar(255) DEFAULT NULL COMMENT '推送内容',
  `intent` varchar(255) DEFAULT NULL COMMENT '推送意图',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_taskid` (`task_id`),
  KEY `idx_topush` (`to_push`),
  KEY `idx_plansendtime_status` (`plan_send_time`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推送发送任务明细';
