
-- ----------------------------
-- Table structure for bu_ali_pay
-- ----------------------------
DROP TABLE IF EXISTS `bu_ali_pay`;
CREATE TABLE `bu_ali_pay` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(11) NOT NULL COMMENT 'bu_user_info.id',
  `appid` varchar(32) NOT NULL COMMENT '应用ID',
  `private_key` varchar(2000) DEFAULT NULL COMMENT '私钥',
  `pub_key` varchar(1000) DEFAULT NULL COMMENT '公钥',
  `notify_url` varchar(256) DEFAULT NULL COMMENT '通知地址',
  `out_trade_no` varchar(32) NOT NULL COMMENT '商户订单号',
  `subject` varchar(128) NOT NULL COMMENT '商品标题',
  `total_amount` decimal(12,2) NOT NULL COMMENT '订单金额(元)',
  `pay_body` varchar(2049) DEFAULT NULL COMMENT '支付体（前端使用）',
  `trade_status` varchar(20) DEFAULT NULL COMMENT '交易结果(TRADE_CLOSED:交易关闭,TRADE_FINISHED:交易完结,TRADE_SUCCESS:支付成功,WAIT_BUYER_PAY:交易创建)',
  `trade_no` varchar(64) DEFAULT NULL COMMENT '支付宝交易凭证号',
  `gmt_payment` varchar(32) DEFAULT NULL COMMENT '交易支付时间',
  `pay_notify_id` int(11) DEFAULT NULL COMMENT '关联bu_pay_notify ID',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_otn` (`out_trade_no`) USING HASH,
  KEY `idx_ui` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='支付宝支付表';

-- ----------------------------
-- Table structure for bu_ali_pay_refund
-- ----------------------------
DROP TABLE IF EXISTS `bu_ali_pay_refund`;
CREATE TABLE `bu_ali_pay_refund` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `appid` varchar(32) NOT NULL COMMENT '应用ID',
  `private_key` varchar(2000) DEFAULT NULL COMMENT '私钥',
  `pub_key` varchar(1000) DEFAULT NULL COMMENT '公钥',
  `out_trade_no` varchar(32) NOT NULL COMMENT '商户订单号',
  `out_request_no` varchar(64) NOT NULL COMMENT '商户退款单号',
  `refund_amount` decimal(12,2) NOT NULL COMMENT '退款金额(元)',
  `trade_status` varchar(20) DEFAULT NULL COMMENT '交易结果(TRADE_CLOSED:交易关闭,TRADE_FINISHED:交易完结,TRADE_SUCCESS:支付成功,WAIT_BUYER_PAY:交易创建)',
  `out_biz_no` varchar(20) DEFAULT NULL COMMENT '外部业务号(有值认为是退款)',
  `pay_notify_id` int(11) DEFAULT NULL COMMENT '关联bu_pay_notify ID',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_rn` (`out_request_no`) USING HASH,
  KEY `idx_otn` (`out_trade_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='支付宝支付退款表';

-- ----------------------------
-- Table structure for bu_financial_flow
-- ----------------------------
DROP TABLE IF EXISTS `bu_financial_flow`;
CREATE TABLE `bu_financial_flow` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_code` varchar(32) NOT NULL COMMENT '订单编号',
  `trade_no` varchar(64) NOT NULL COMMENT '渠道订单号',
  `business_source` varchar(64) NOT NULL COMMENT '业务来源',
  `amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '进出账金额(元)',
  `trade_way` varchar(32) NOT NULL COMMENT '交易方式(wx_pay:微信支付,ali_pay:支付宝支付,mch_pay:微信企业打款)',
  `merchant_no` varchar(32) NOT NULL COMMENT '商户号',
  `remarks` varchar(255) NOT NULL COMMENT '备注信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_order_code` (`order_code`),
  KEY `idx_trade_way` (`trade_way`),
  KEY `idx_business_source` (`business_source`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务流水表';

-- ----------------------------
-- Table structure for bu_inner_callback
-- ----------------------------
DROP TABLE IF EXISTS `bu_inner_callback`;
CREATE TABLE `bu_inner_callback` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `url` varchar(128) NOT NULL COMMENT '回调地址',
  `json_params` varchar(2048) DEFAULT NULL COMMENT 'JSON参数',
  `json_result` varchar(128) DEFAULT NULL COMMENT 'JSON结果',
  `processor_bean_name` varchar(384) DEFAULT NULL COMMENT '处理器bean名称',
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='内部回调表';

-- ----------------------------
-- Table structure for bu_order_pay
-- ----------------------------
DROP TABLE IF EXISTS `bu_order_pay`;
CREATE TABLE `bu_order_pay` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(11) NOT NULL COMMENT 'bu_user_info.id',
  `order_code` varchar(32) NOT NULL COMMENT '订单编号',
  `business_type` varchar(16) NOT NULL COMMENT '业务类型(nomal:普通下单,kill:秒杀下单,member:购买会员)',
  `method` varchar(8) NOT NULL COMMENT '支付方式(ali:支付宝,wx:微信,ios:苹果,quick:云闪付)',
  `appid` varchar(32) NOT NULL COMMENT '支付应用ID',
  `amount` decimal(12,2) NOT NULL COMMENT '金额(元)',
  `body` varchar(127) NOT NULL COMMENT '商品描述',
  `status` varchar(8) NOT NULL COMMENT '状态(waitpay:待支付,closed:已关闭,payed:已支付)',
  `notify_url` varchar(128) DEFAULT NULL COMMENT '通知地址(关闭/支付订单都会通知)',
  `notify_attach` varchar(256) DEFAULT NULL COMMENT '通知地址的附加数据',
  `pay_time` datetime DEFAULT NULL COMMENT '关闭/支付时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注(多个使用/分隔)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_order_code` (`order_code`) USING BTREE,
  KEY `idx_userid` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='订单支付表';

-- ----------------------------
-- Table structure for bu_order_pay_refund
-- ----------------------------
DROP TABLE IF EXISTS `bu_order_pay_refund`;
CREATE TABLE `bu_order_pay_refund` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(11) NOT NULL COMMENT 'bu_user_info.id',
  `refund_order_code` varchar(32) NOT NULL COMMENT '退款订单编号',
  `order_code` varchar(32) NOT NULL COMMENT '订单编号',
  `business_type` varchar(16) NOT NULL COMMENT '业务类型(user:用户申请,sys_auto:系统自动退款,amdin:管理后台申请)',
  `method` varchar(8) NOT NULL COMMENT '支付方式(ali:支付宝,wx:微信,ios:苹果,quick:云闪付)',
  `amount` decimal(12,2) NOT NULL COMMENT '金额(元)',
  `refund_amount` decimal(12,2) NOT NULL COMMENT '退款金额(元)',
  `status` varchar(16) NOT NULL COMMENT '状态(wait_apply:待申请,apply_success:申请成功,apply_fail:申请失败,refund_success:退款成功,refund_fail:退款失败)',
  `notify_url` varchar(128) DEFAULT NULL COMMENT '通知地址(关闭/支付订单都会通知)',
  `notify_attach` varchar(256) DEFAULT NULL COMMENT '通知地址的附加数据',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注(多个使用/分隔)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_refundordercode` (`refund_order_code`) USING BTREE,
  KEY `idx_ordercode` (`order_code`) USING HASH,
  KEY `idx_userid` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='订单支付退款表';

-- ----------------------------
-- Table structure for bu_pay_notify
-- ----------------------------
DROP TABLE IF EXISTS `bu_pay_notify`;
CREATE TABLE `bu_pay_notify` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `method` varchar(8) NOT NULL COMMENT '支付方式(ali:支付宝,wx:微信,ios:苹果,quick:云闪付)',
  `notify_data` varchar(2048) DEFAULT NULL COMMENT '通知数据',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付通知表';

-- ----------------------------
-- Table structure for bu_pay_refund_apply
-- ----------------------------
DROP TABLE IF EXISTS `bu_pay_refund_apply`;
CREATE TABLE `bu_pay_refund_apply` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_code` varchar(32) NOT NULL COMMENT '退款订单号',
  `old_order_code` varchar(32) NOT NULL COMMENT '原订单号',
  `amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '退款金额(元)',
  `business_type` varchar(16) NOT NULL COMMENT '业务类型(user:用户申请,sys_auto:系统自动退款,amdin:管理后台申请)',
  `verify_status` int(11) NOT NULL DEFAULT '0' COMMENT '审核状态(1:待审核,21:通过,22:驳回)',
  `refund_status` int(11) NOT NULL DEFAULT '0' COMMENT '退款状态(1:未退款,21:退款驳回(END),31:处理中,41:申请成功,42:申请失败(END),51:退款成功(END),52:退款失败(END))',
  `reason` varchar(64) NOT NULL COMMENT '退款原因',
  `attach` varchar(256) NOT NULL COMMENT '通知地址的附加数据',
  `remark` varchar(255) NOT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_ordercode` (`order_code`),
  KEY `idx_rs_vs` (`refund_status`,`verify_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退款申请表';

-- ----------------------------
-- Table structure for bu_wx_pay
-- ----------------------------
DROP TABLE IF EXISTS `bu_wx_pay`;
CREATE TABLE `bu_wx_pay` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(11) NOT NULL COMMENT 'bu_user_info.id',
  `appid` varchar(32) NOT NULL COMMENT '小程序ID|公众账号ID|应用ID',
  `mchid` varchar(32) NOT NULL COMMENT '商户号',
  `mch_key` varchar(128) DEFAULT NULL COMMENT '商户密钥',
  `key_path` varchar(255) DEFAULT NULL COMMENT '证书路径',
  `nonce_str` varchar(32) NOT NULL COMMENT '随机字符串',
  `sign` varchar(64) DEFAULT NULL COMMENT '签名',
  `body` varchar(128) NOT NULL COMMENT '商品描述',
  `out_trade_no` varchar(32) NOT NULL COMMENT '商户订单号',
  `total_fee` int(11) NOT NULL COMMENT '订单金额(分)',
  `spbill_create_ip` varchar(64) NOT NULL COMMENT '终端IP',
  `notify_url` varchar(256) NOT NULL COMMENT '通知地址',
  `trade_type` varchar(16) NOT NULL COMMENT '小程序:JSAPI,H5:MWEB,APP:APP',
  `product_id` varchar(32) DEFAULT NULL COMMENT '商品ID(trade_type=NATIVE，此参数必传)',
  `openid` varchar(128) DEFAULT NULL COMMENT '用户标识(trade_type=JSAPI,此参数必传)',
  `return_code` varchar(16) DEFAULT NULL COMMENT '返回状态码(SUCCESS/FAIL)',
  `return_msg` varchar(128) DEFAULT NULL COMMENT '返回信息',
  `result_code` varchar(16) DEFAULT NULL COMMENT '业务结果(SUCCESS/FAIL)',
  `err_code` varchar(32) DEFAULT NULL COMMENT '错误代码',
  `err_code_des` varchar(128) DEFAULT NULL COMMENT '错误代码描述',
  `prepay_id` varchar(64) DEFAULT NULL COMMENT '预支付交易会话标识',
  `mweb_url` varchar(255) DEFAULT NULL COMMENT '支付跳转链接',
  `code_url` varchar(255) DEFAULT NULL COMMENT '二维码链接',
  `notify_result_code` varchar(16) DEFAULT NULL COMMENT '回调-业务结果(SUCCESS/FAIL)',
  `notify_err_code` varchar(32) DEFAULT NULL COMMENT '回调-错误代码',
  `notify_err_code_des` varchar(128) DEFAULT NULL COMMENT '回调-错误代码描述',
  `transaction_id` varchar(32) DEFAULT NULL COMMENT '微信支付订单号',
  `time_end` varchar(16) DEFAULT NULL COMMENT '支付完成时间(yyyyMMddHHmmss)',
  `pay_notify_id` int(11) DEFAULT NULL COMMENT '关联bu_pay_notify ID',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_otn` (`out_trade_no`) USING HASH,
  KEY `idx_ui` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='微信支付表';

-- ----------------------------
-- Table structure for bu_wx_pay_refund
-- ----------------------------
DROP TABLE IF EXISTS `bu_wx_pay_refund`;
CREATE TABLE `bu_wx_pay_refund` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `appid` varchar(32) NOT NULL COMMENT '小程序ID|公众账号ID|应用ID',
  `mchid` varchar(32) NOT NULL COMMENT '商户号',
  `mch_key` varchar(128) DEFAULT NULL COMMENT '商户密钥',
  `key_path` varchar(255) DEFAULT NULL COMMENT '证书路径',
  `nonce_str` varchar(32) NOT NULL COMMENT '随机字符串',
  `sign` varchar(64) DEFAULT NULL COMMENT '签名',
  `out_trade_no` varchar(32) NOT NULL COMMENT '商户订单号',
  `out_refund_no` varchar(32) NOT NULL COMMENT '商户退款单号',
  `total_fee` int(11) NOT NULL COMMENT '订单金额(分)',
  `refund_fee` int(11) NOT NULL COMMENT '退款金额(分)',
  `return_code` varchar(16) DEFAULT NULL COMMENT '返回状态码(SUCCESS/FAIL)',
  `return_msg` varchar(128) DEFAULT NULL COMMENT '返回信息',
  `result_code` varchar(16) DEFAULT NULL COMMENT '业务结果(SUCCESS/FAIL)',
  `err_code` varchar(32) DEFAULT NULL COMMENT '错误代码',
  `err_code_des` varchar(128) DEFAULT NULL COMMENT '错误代码描述',
  `refund_id` varchar(32) DEFAULT NULL COMMENT '微信退款单号',
  `cash_fee` int(11) DEFAULT NULL COMMENT '现金支付金额(分)',
  `refund_status` varchar(16) DEFAULT NULL COMMENT '回调-退款状态(SUCCESS/CHANGE/REFUNDCLOSE)',
  `pay_notify_id` int(11) DEFAULT NULL COMMENT '关联bu_pay_notify ID',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_orn` (`out_refund_no`) USING HASH,
  KEY `idx_otn` (`out_trade_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='微信支付退款表';
