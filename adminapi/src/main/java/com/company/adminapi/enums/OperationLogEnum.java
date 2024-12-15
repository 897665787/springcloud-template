package com.company.adminapi.enums;

public interface OperationLogEnum {

	/**
	 * 操作状态
	 */
	enum BusinessStatus {
		/**
		 * 成功
		 */
		SUCCESS,

		/**
		 * 失败
		 */
		FAIL,
	}

	/**
	 * 业务操作类型
	 */
	enum BusinessType {
		/**
		 * 其它
		 */
		OTHER,

		/**
		 * 新增
		 */
		INSERT,

		/**
		 * 修改
		 */
		UPDATE,

		/**
		 * 删除
		 */
		DELETE,

		/**
		 * 授权
		 */
		GRANT,

		/**
		 * 导出
		 */
		EXPORT,

		/**
		 * 导入
		 */
		IMPORT,

		/**
		 * 强退
		 */
		FORCE,

		/**
		 * 生成代码
		 */
		CODE_GEN,

		/**
		 * 清空数据
		 */
		CLEAN,
	}

}
