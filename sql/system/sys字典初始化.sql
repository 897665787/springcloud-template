-- ----------------------------
-- 管理后台字典初始化（若依前端依赖以下字典类型）
-- 说明：sys_dict_data.dict_code 为前端使用的字典值，dict_value 为展示文案
-- ----------------------------

INSERT INTO `sys_dict_type` (`dict_name`, `dict_type`, `status`, `dict_remark`, `remark`) VALUES
('系统开关', 'sys_normal_disable', 'ON', '正常/停用', ''),
('菜单显示状态', 'sys_show_hide', 'ON', '显示/隐藏', ''),
('用户性别', 'sys_user_sex', 'ON', '性别列表', ''),
('系统是否', 'sys_yes_no', 'ON', '是否列表', ''),
('通知类型', 'sys_notice_type', 'ON', '通知类型列表', ''),
('通知状态', 'sys_notice_status', 'ON', '通知状态列表', ''),
('操作类型', 'sys_oper_type', 'ON', '操作类型列表', ''),
('系统状态', 'sys_common_status', 'ON', '登录/操作状态列表', ''),
('任务分组', 'sys_job_group', 'ON', '任务分组列表', ''),
('任务状态', 'sys_job_status', 'ON', '任务状态列表', '')
ON DUPLICATE KEY UPDATE `dict_name` = VALUES(`dict_name`);

INSERT INTO `sys_dict_data` (`dict_type`, `dict_code`, `dict_value`, `dict_sort`, `is_default`, `status`, `dict_remark`, `remark`) VALUES
-- 系统开关：前端字典值为 0/1，由前端适配层转换为后端 ON/OFF
('sys_normal_disable', '0', '正常', 1, 'Y', 'ON', '正常', ''),
('sys_normal_disable', '1', '停用', 2, 'N', 'ON', '停用', ''),
-- 菜单显示状态：前端字典值为 0-显示/1-隐藏，由前端适配层转换为后端 1/0
('sys_show_hide', '0', '显示', 1, 'Y', 'ON', '显示', ''),
('sys_show_hide', '1', '隐藏', 2, 'N', 'ON', '隐藏', ''),
-- 用户性别
('sys_user_sex', '0', '男', 1, 'Y', 'ON', '性别男', ''),
('sys_user_sex', '1', '女', 2, 'N', 'ON', '性别女', ''),
('sys_user_sex', '2', '未知', 3, 'N', 'ON', '性别未知', ''),
-- 系统是否
('sys_yes_no', 'Y', '是', 1, 'Y', 'ON', '系统默认是', ''),
('sys_yes_no', 'N', '否', 2, 'N', 'ON', '系统默认否', ''),
-- 通知类型
('sys_notice_type', '1', '通知', 1, 'Y', 'ON', '通知', ''),
('sys_notice_type', '2', '公告', 2, 'N', 'ON', '公告', ''),
-- 通知状态
('sys_notice_status', '0', '正常', 1, 'Y', 'ON', '正常状态', ''),
('sys_notice_status', '1', '关闭', 2, 'N', 'ON', '关闭状态', ''),
-- 操作类型（与 sys_oper_log.business_type 的 ordinal 对齐）
('sys_oper_type', '0', '其它', 1, 'N', 'ON', '其它', ''),
('sys_oper_type', '1', '新增', 2, 'N', 'ON', '新增', ''),
('sys_oper_type', '2', '修改', 3, 'N', 'ON', '修改', ''),
('sys_oper_type', '3', '删除', 4, 'N', 'ON', '删除', ''),
('sys_oper_type', '4', '授权', 5, 'N', 'ON', '授权', ''),
('sys_oper_type', '5', '导出', 6, 'N', 'ON', '导出', ''),
('sys_oper_type', '6', '导入', 7, 'N', 'ON', '导入', ''),
('sys_oper_type', '7', '强退', 8, 'N', 'ON', '强退', ''),
('sys_oper_type', '8', '生成代码', 9, 'N', 'ON', '生成代码', ''),
('sys_oper_type', '9', '清空数据', 10, 'N', 'ON', '清空数据', ''),
-- 系统状态（登录日志/操作日志）
('sys_common_status', '0', '成功', 1, 'N', 'ON', '成功', ''),
('sys_common_status', '1', '失败', 2, 'N', 'ON', '失败', ''),
-- 任务分组
('sys_job_group', 'DEFAULT', '默认', 1, 'Y', 'ON', '默认分组', ''),
('sys_job_group', 'SYSTEM', '系统', 2, 'N', 'ON', '系统分组', ''),
-- 任务状态
('sys_job_status', '0', '正常', 1, 'Y', 'ON', '正常', ''),
('sys_job_status', '1', '暂停', 2, 'N', 'ON', '暂停', '')
ON DUPLICATE KEY UPDATE `dict_value` = VALUES(`dict_value`), `dict_sort` = VALUES(`dict_sort`), `status` = VALUES(`status`);
