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

CREATE TABLE `push_device_bind` (
 `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
 `deviceid` varchar(64) NOT NULL COMMENT '设备ID（手机唯一）',
--  `channel` varchar(16) NOT NULL COMMENT '发送渠道(log:打印日志（测试）,ali:阿里云,tencent:腾讯云)',
 `channel_id` varchar(32) NOT NULL COMMENT '设备ID（推送平台唯一）',
 `device_type` varchar(16) NOT NULL COMMENT '设备类型(Android:安卓,iOS:苹果)',
 `agent` varchar(16) NOT NULL COMMENT '设备类型(Android:安卓,iOS:苹果)',

 `remark` varchar(255) DEFAULT NULL COMMENT '备注',
 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
 PRIMARY KEY (`id`) USING BTREE,
 UNIQUE KEY `uniq_deviceid_channel` (`deviceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推送绑定设备';

CREATE TABLE `device_info` (
 `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
 `deviceid` varchar(64) NOT NULL COMMENT '设备ID',
 `platform` varchar(32) DEFAULT NULL COMMENT '平台：app(APP)、mini(微信小程序)、h5(H5页面)、alimini(支付宝小程序)、alipaymini(支付宝小程序内H5)',
 `operator` varchar(32) DEFAULT NULL COMMENT '操作系统：ios(iOS)、mac(iOS)、android(Android)、win(Windows,如果获取不到可以不要)、devtools(小程序开发工具)',
 `channel` varchar(32) DEFAULT NULL COMMENT '渠道：wx(微信小程序)、ali(支付宝小程序)、dev(开发包)、sit(测试包)、uat(验收包)、ios(苹果应用商店)、xiaomi(小米应用商店)、huawei(华为应用商店)等',
 `version` varchar(32) DEFAULT NULL COMMENT '版本号',
 `requestip` varchar(32) DEFAULT NULL COMMENT '请求IP',
 `request_user_agent` varchar(255) DEFAULT NULL COMMENT '请求UserAgent',

 `remark` varchar(255) DEFAULT NULL COMMENT '备注',
 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
 PRIMARY KEY (`id`) USING BTREE,
 UNIQUE KEY `uniq_deviceid` (`deviceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备信息';


CREATE TABLE `user_device` (
                               `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
                               `user_id` int(11) NOT NULL COMMENT 'bu_user_info.id',
                               `deviceid` varchar(64) NOT NULL COMMENT '设备ID',
                               `last_active_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后活跃时间',

                               `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                               `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE KEY `uniq_userid_deviceid` (`user_id`,`deviceid`)
                                   KEY `idx_userid` (`user_id`,`last_active_time`),
                               KEY `idx_deviceid` (`deviceid`,`last_active_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户设备关系表';

-- select deviceid from user_device where user_id = 1 order by last_active_time desc limit 1;
-- select user_id from user_device where deviceid = 'xxx' order by last_active_time desc limit 1;

