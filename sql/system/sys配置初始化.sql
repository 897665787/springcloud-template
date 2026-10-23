-- ----------------------------
-- 管理后台参数配置初始化
-- 说明：代码中通过 sys_config.code 读取，禁止在代码里写死
-- ----------------------------

INSERT INTO `sys_config` (`name`, `code`, `value`, `config_remark`, `remark`) VALUES
('用户初始密码', 'sys.user.initPassword', '12345678', '新建用户、重置密码时使用的初始密码', '')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `config_remark` = VALUES(`config_remark`);
