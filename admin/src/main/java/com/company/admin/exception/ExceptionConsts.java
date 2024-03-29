package com.company.admin.exception;

import com.company.common.exception.BusinessException;

public interface ExceptionConsts {
	BusinessException PARAM_FORMAT_ERROR = new BusinessException(600, "请求参数格式错误");
	BusinessException SQL_DUPLICATE_KEY = new BusinessException(602, "数据已存在");
	BusinessException FAILURE = new BusinessException(999, "系统错误");

	/* 系统 */
	BusinessException SYSTEM_ERROR = new BusinessException(10000, "系统异常");
	BusinessException SYSTEM_BUSY = new BusinessException(10001, "系统繁忙");
	BusinessException MISSING_PARAM = new BusinessException(10002, "缺少必要参数");
	BusinessException ILLEGAL_OPERATION = new BusinessException(10003, "非法操作");
	BusinessException PAGINATION_ERROR = new BusinessException(10004, "请填写分页参数[offset,limit]或[page]");

	/* 文章 */
	BusinessException ARTICLE_CATEGORY_EXIST = new BusinessException(10300, "文章分类已存在");
	BusinessException ARTICLE_CATEGORY_LOCKED = new BusinessException(10301, "文章分类被锁定");
	BusinessException ARTICLE_CATEGORY_USED = new BusinessException(10302, "文章分类被使用");
	BusinessException ARTICLE_CATEGORY_NOT_EXIST = new BusinessException(10303, "文章分类不存在");
	BusinessException ARTICLE_CATEGORY_NOT_SELF_OR_SUB = new BusinessException(10304, "不能选择自己或自己的下级作为父级");
	BusinessException ARTICLE_NOT_EXIST = new BusinessException(10305, "文章不存在");

	/* 图片 */
	BusinessException IMAGE_CATEGORY_EXIST = new BusinessException(10400, "图片分类已存在");
	BusinessException IMAGE_CATEGORY_LOCKED = new BusinessException(10401, "图片分类被锁定");
	BusinessException IMAGE_CATEGORY_USED = new BusinessException(10402, "图片分类被使用");
	BusinessException IMAGE_CATEGORY_NOT_EXIST = new BusinessException(10403, "图片分类不存在");
	BusinessException IMAGE_CATEGORY_NOT_SELF_OR_SUB = new BusinessException(10304, "不能选择自己或自己的下级作为父级");

	/* 系统权限 */
	BusinessException SEC_RESOURCE_EXIST = new BusinessException(10000, "资源已存在");
	BusinessException SEC_RESOURCE_NOT_EXIST = new BusinessException(10001, "资源不存在");
	BusinessException SEC_RESOURCE_USED = new BusinessException(10002, "资源被使用");
	BusinessException SEC_RESOURCE_IMPORT_ERROR = new BusinessException(10003, "资源导入失败，{}");
	BusinessException SEC_STAFF_EXIST = new BusinessException(10009, "员工已存在");
	BusinessException SEC_STAFF_NOT_EXIST = new BusinessException(10010, "员工不存在");
	BusinessException SEC_STAFF_NOT_ENABLE = new BusinessException(10011, "员工被禁用");
	BusinessException SEC_STAFF_ACCESS_DENY = new BusinessException(10012, "您的访问被拒绝");
	BusinessException SEC_STAFF_PASSWORD_ERROR = new BusinessException(10013, "密码错误");
	BusinessException SEC_STAFF_CONCURRENT_BEYOND = new BusinessException(10014, "该账号超出最大登录人数限制");
	BusinessException SEC_STAFF_SESSION_TIMEOUT = new BusinessException(10015, "会话超时，请重新登录");
	BusinessException SEC_STAFF_MOBILE_EXIST = new BusinessException(10017, "手机号已被注册");
	BusinessException SEC_STAFF_USERNAME_EXIST = new BusinessException(10018, "用户名已被使用");

	/* App初始化 */
	BusinessException APPINIT_EXIST = new BusinessException(10700, "App初始化已存在");
	BusinessException APPINIT_NOT_EXIST = new BusinessException(10701, "App初始化不存在");

	/* 数据字典 */
	BusinessException DICT_DATA_IMPORT_ERROR = new BusinessException(10507, "字典数据导入失败，{}");

	/* 系统配置 */
	BusinessException SYSTEM_CONFIG_EXIST = new BusinessException(10200, "系统配置已存在");
	BusinessException SYSTEM_CONFIG_NOT_EXIST = new BusinessException(10201, "系统配置不存在");

	/* 版本 */
	BusinessException VERSION_IS_LATEST = new BusinessException(10602, "已经是最新版本");

	/* 短信验证码 */
	BusinessException DIGITAL_CAPTCHA_NOT_EXPIRE = new BusinessException(10900, "数字验证码未过期，剩余{}秒");
	BusinessException DIGITAL_CAPTCHA_INVALID = new BusinessException(10901, "数字验证码无效");
	BusinessException GRAPH_CAPTCHA_INVALID = new BusinessException(10902, "图形验证码校验失败");
	BusinessException SMS_ERROR = new BusinessException(11100, "短信发送失败{}");

	/* 开放平台 */
	BusinessException WECHAT_AUTHORIZE_FAIL = new BusinessException(11200, "微信授权失败");
	BusinessException WECHAT_GET_USER_FAIL = new BusinessException(11201, "从微信获取用户信息失败");

	/* 用户 */
	BusinessException TOKEN_INVALID = new BusinessException(11000, "令牌无效");
	BusinessException USER_EXIST = new BusinessException(20300, "用户已存在");
	BusinessException USER_NOT_EXIST = new BusinessException(20301, "用户不存在");
	BusinessException USER_NOT_ENABLE = new BusinessException(20302, "用户被冻结");
	BusinessException MOBILE_BIND = new BusinessException(20303, "手机号已被绑定");
	BusinessException UNIONID_IS_NULL = new BusinessException(20304, "微信unionid为空");
	BusinessException OPENID_IS_NULL = new BusinessException(20305, "微信openid为空");
	BusinessException LEVEL_TITLE_EXISTED = new BusinessException(20306, "已存在相同等级或相同名称或相同经验值的的等级称号");
	BusinessException LEVEL_TITLE_NOT_EXIST = new BusinessException(20307, "等级称号不存在");
	BusinessException DATA_NOT_EXISTED = new BusinessException(20308, "数据不存在");

	/* excel */
	BusinessException EXCEL_TEMPLATE_FORMAT_WRONG = new BusinessException(10305, "模板格式错误，请检查");
}
