
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
