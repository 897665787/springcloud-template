-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '名称',
  `code` varchar(32) NOT NULL DEFAULT '' COMMENT '编码',
  `value` varchar(512) NOT NULL DEFAULT '' COMMENT '值',
  `config_remark` varchar(255) NOT NULL DEFAULT '' COMMENT '参数备注',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '参数配置表';

-- 参数配置国际化翻译表
CREATE TABLE `sys_config_i18n` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `sys_config_id` int(11) NOT NULL COMMENT 'sys_config.id',
  `locale` varchar(8) NOT NULL DEFAULT '' COMMENT '地区编码',
  `value` varchar(512) NOT NULL DEFAULT '' COMMENT '值',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_sysconfigid_locale` (`sys_config_id`,`locale`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='参数配置-国际化';

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `parent_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '父部门id',
  `parent_ids` varchar(255) NOT NULL DEFAULT '' COMMENT '父部门id列表',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int(4) NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `status` varchar(8) NOT NULL DEFAULT '' COMMENT '部门状态(ON:正常,OFF:停用)',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '部门表';

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dict_type` varchar(32) NOT NULL DEFAULT '' COMMENT '字典类型',
  `dict_code` varchar(32) NOT NULL DEFAULT '' COMMENT '字典编码',
  `dict_value` varchar(255) NOT NULL DEFAULT '' COMMENT '字典值',
  `dict_sort` int(4) NOT NULL DEFAULT 0 COMMENT '字典排序',
  `is_default` char(1) NULL DEFAULT 'N' COMMENT '是否默认(Y:是,N:否)',
  `status` varchar(8) NULL DEFAULT '0' COMMENT '状态(ON:正常,OFF:停用)',
  `dict_remark` varchar(255) NOT NULL DEFAULT '' COMMENT '字典备注',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_dicttype_dictcode`(`dict_type`, `dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '字典数据表';

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dict_name` varchar(32) NOT NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(32) NOT NULL DEFAULT '' COMMENT '字典类型',
  `status` varchar(8) NOT NULL DEFAULT '0' COMMENT '状态(ON:正常,OFF:停用)',
  `dict_remark` varchar(255) NOT NULL DEFAULT '' COMMENT '字典备注',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_dicttype`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '字典类型表';

-- ----------------------------
-- Table structure for sys_logininfo
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfo`;
CREATE TABLE `sys_logininfo`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sys_user_id` int(11) NOT NULL COMMENT 'sys_user.id',
  `login_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `account` varchar(32) NOT NULL DEFAULT '' COMMENT '登录账户',
  `device` varchar(16) NOT NULL DEFAULT '' COMMENT '设备(ADMIN:admin端)',
  `platform` varchar(32) NOT NULL DEFAULT '' COMMENT '平台',
  `operator` varchar(32) NOT NULL DEFAULT '' COMMENT '操作系统',
  `version` varchar(32) NOT NULL DEFAULT '' COMMENT '版本',
  `deviceid` varchar(32) NOT NULL DEFAULT '' COMMENT '设备ID',
  `channel` varchar(32) NOT NULL DEFAULT '' COMMENT '渠道',
  `ip` varchar(32) NOT NULL DEFAULT '' COMMENT 'ip',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '登录地区(根据ip获得)',
  `source` varchar(32) NOT NULL DEFAULT '' COMMENT '来源',
  `lang` varchar(32) NOT NULL DEFAULT '' COMMENT '语言',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sysuserid`(`sys_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '用户登录日志表';

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `parent_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '父菜单ID',
  `icon` varchar(128) NOT NULL DEFAULT '' COMMENT '菜单图标',
  `menu_name` varchar(64) NOT NULL DEFAULT '' COMMENT '菜单名称',
  `order_num` int(4) NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) NOT NULL DEFAULT '' COMMENT '路由地址',
  `redirect` varchar(255) NOT NULL DEFAULT '' COMMENT '重定向',
  `component` varchar(255) NOT NULL DEFAULT '' COMMENT '组件路径',
  `query` varchar(255) NOT NULL DEFAULT '' COMMENT '路由参数',
  `menu_type` char(1) NOT NULL DEFAULT '' COMMENT '菜单类型(M:目录,C:菜单,F:按钮)',
  `status` varchar(8) NOT NULL DEFAULT 'OFF' COMMENT '菜单状态(ON:正常,OFF:停用)',
  `visible` tinyint(4) NOT NULL DEFAULT 0 COMMENT '显示状态(1:显示,0:隐藏)',
  `perms` varchar(128) NOT NULL DEFAULT '' COMMENT '权限标识',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '菜单权限表';

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sys_user_id` int(11) NOT NULL COMMENT 'sys_user.id',
  `title` varchar(64) NOT NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) NOT NULL DEFAULT 0 COMMENT '业务类型(0:其它,1:新增,2:修改,3:删除)',
  `method` varchar(128) NOT NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) NOT NULL DEFAULT '' COMMENT '请求方式',
  `oper_url` varchar(255) NOT NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) NOT NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) NOT NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) NOT NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) NOT NULL DEFAULT '' COMMENT '返回参数',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '操作状态(0:正常,1:异常)',
  `error_msg` varchar(2000) NOT NULL DEFAULT '' COMMENT '错误消息',
  `cost_time` int(11) NOT NULL DEFAULT 0 COMMENT '耗时(毫秒)',
  `oper_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_businesstype`(`business_type`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_createtime`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '操作日志记录';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_name` varchar(32) NOT NULL DEFAULT '' COMMENT '角色名称',
  `role_key` varchar(128) NOT NULL DEFAULT '' COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `data_scope` char(1) NULL DEFAULT '1' COMMENT '数据范围(1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限)',
  `status` varchar(8) NOT NULL DEFAULT '' COMMENT '角色状态(ON:正常,OFF:停用)',
  `role_remark` varchar(255) NOT NULL DEFAULT '' COMMENT '角色备注',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '角色信息表';

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '角色ID',
  `dept_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '部门ID',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_roleid_deptid`(`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '角色和菜单关联表';

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '角色ID',
  `menu_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '菜单ID',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_roleid_menuid`(`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '角色和菜单关联表';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `account` varchar(32) NOT NULL DEFAULT '' COMMENT '账号',
  `nickname` varchar(32) NOT NULL DEFAULT '' COMMENT '昵称',
  `email` varchar(64) NOT NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(32) NOT NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) NOT NULL DEFAULT '0' COMMENT '用户性别(0:男,1:女,2:未知)',
  `avatar` varchar(255) NOT NULL DEFAULT '' COMMENT '头像地址',
  `status` varchar(8) NOT NULL DEFAULT 'ON' COMMENT '帐号状态(ON:正常,OFF:停用)',
  `dept_id` int(11) NOT NULL DEFAULT 0 COMMENT '部门ID',
  `user_remark` varchar(255) NOT NULL DEFAULT '' COMMENT '用户备注',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_account`(`account`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '用户信息表';

-- ----------------------------
-- Table structure for sys_user_password
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_password`;
CREATE TABLE `sys_user_password`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sys_user_id` int(10) UNSIGNED NOT NULL COMMENT 'sys_user.id',
  `password` varchar(128) NOT NULL DEFAULT '' COMMENT '密码',
  `expire_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  `expire_login_times` int(11) NOT NULL DEFAULT 0 COMMENT '过期后登录次数',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sysuserid`(`sys_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '用户密码表';

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户ID',
  `role_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '角色ID',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_userid_roleid`(`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '用户和角色关联表';

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `post_code` varchar(64) NOT NULL DEFAULT '' COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL DEFAULT '' COMMENT '岗位名称',
  `post_sort` int(4) NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `status` varchar(8) NOT NULL DEFAULT 'ON' COMMENT '状态(ON:正常,OFF:停用)',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_postcode`(`post_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '岗位信息表';

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `notice_title` varchar(50) NOT NULL DEFAULT '' COMMENT '公告标题',
  `notice_type` char(1) NOT NULL DEFAULT '' COMMENT '公告类型(1:通知,2:公告)',
  `notice_content` text NULL COMMENT '公告内容',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '公告状态(0:正常,1:关闭)',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '通知公告表';

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `job_name` varchar(64) NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL DEFAULT '' COMMENT '调用目标字符串(beanName.methodName)',
  `cron_expression` varchar(255) NOT NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) NOT NULL DEFAULT '3' COMMENT '计划执行错误策略(1:立即执行,2:执行一次,3:放弃执行)',
  `concurrent` char(1) NOT NULL DEFAULT '1' COMMENT '是否并发执行(0:允许,1:禁止)',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '状态(0:正常,1:暂停)',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '定时任务调度表';

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `job_name` varchar(64) NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL DEFAULT '' COMMENT '调用目标字符串',
  `job_message` varchar(500) NOT NULL DEFAULT '' COMMENT '日志信息',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '执行状态(0:正常,1:失败)',
  `exception_info` varchar(2000) NOT NULL DEFAULT '' COMMENT '异常信息',
  `start_time` datetime NULL DEFAULT NULL COMMENT '执行开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '执行结束时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '定时任务调度日志表';

-- ----------------------------
-- Table structure for sys_gen_table
-- ----------------------------
DROP TABLE IF EXISTS `sys_gen_table`;
CREATE TABLE `sys_gen_table`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `table_name` varchar(200) NOT NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) NOT NULL DEFAULT '' COMMENT '表描述',
  `class_name` varchar(100) NOT NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) NOT NULL DEFAULT 'crud' COMMENT '使用的模板(crud:单表, tree:树表, sub:主子表)',
  `package_name` varchar(100) NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) NOT NULL DEFAULT '0' COMMENT '生成代码方式(0:zip压缩包,1:自定义路径)',
  `gen_path` varchar(200) NULL DEFAULT '/' COMMENT '生成路径(不填默认项目路径)',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_table_name`(`table_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '代码生成业务表';

-- ----------------------------
-- Table structure for sys_gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `sys_gen_table_column`;
CREATE TABLE `sys_gen_table_column`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `table_id` int(11) NULL DEFAULT NULL COMMENT '归属表id',
  `column_name` varchar(200) NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) NULL DEFAULT NULL COMMENT '列类型',
  `java_field` varchar(200) NULL DEFAULT NULL COMMENT 'java属性名',
  `java_type` varchar(500) NULL DEFAULT NULL COMMENT 'java类型',
  `is_pk` char(1) NULL DEFAULT NULL COMMENT '是否主键(1:是,0:否)',
  `is_required` char(1) NULL DEFAULT NULL COMMENT '是否必填(1:是,0:否)',
  `is_insert` char(1) NULL DEFAULT NULL COMMENT '是否为插入字段(1:是,0:否)',
  `is_edit` char(1) NULL DEFAULT NULL COMMENT '是否编辑字段(1:是,0:否)',
  `is_list` char(1) NULL DEFAULT NULL COMMENT '是否列表字段(1:是,0:否)',
  `is_query` char(1) NULL DEFAULT NULL COMMENT '是否查询字段(1:是,0:否)',
  `query_type` varchar(200) NULL DEFAULT 'EQ' COMMENT '查询方式(EQ:等于,NE:不等于,GT:大于,GTE:大于等于,LT:小于,LTE:小于等于,LIKE:模糊,BETWEEN:范围)',
  `html_type` varchar(200) NULL DEFAULT NULL COMMENT '显示类型(input:文本框,textarea:文本域,select:下拉框,radio:单选框,checkbox:复选框,datetime:日期控件,imageUpload:图片上传,fileUpload:文件上传,editor:富文本控件)',
  `dict_type` varchar(200) NULL DEFAULT '' COMMENT '字典类型',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_table_id`(`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COMMENT = '代码生成业务表字段';
