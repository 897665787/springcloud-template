package com.company.system.innercallback.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.system.entity.InnerCallback;

public interface IInnerCallbackService extends IService<InnerCallback> {
	/**
	 * 使用RestTemplate POST方式进行回调
	 * 
	 * @param postParam
	 *            post参数
	 * @return 成功/失败
	 */
	Boolean postRestTemplate(PostParam postParam);
	
	/**
	 * 使用RestTemplate POST方式进行回调
	 * 
	 * @param id
	 *            主键
	 * @return 成功/失败
	 */
	Boolean postRestTemplate(Integer id);

	/**
	 * 查询回调失败的回调ID
	 * 
	 * @return
	 */
	List<Integer> selectId4CallbackFail();

}
