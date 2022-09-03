package com.company.order.innercallback.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.company.order.entity.InnerCallback;
import com.company.order.enums.InnerCallbackEnum;
import com.company.order.innercallback.processor.bean.ProcessorBeanName;

public interface IInnerCallbackService extends IService<InnerCallback> {
	/**
	 * 使用RestTemplate POST方式进行回调
	 * 
	 * @param notifyUrl
	 *            回调地址
	 * @param jsonParams
	 *            可转化为JSON的参数对象
	 * @return 成功/失败
	 */
	Boolean postRestTemplate(String notifyUrl, Object jsonParams);
	
	/**
	 * 使用RestTemplate POST方式进行回调
	 * 
	 * @param notifyUrl
	 *            回调地址
	 * @param jsonParams
	 *            可转化为JSON的参数对象
	 * @param maxFailure
	 *            最大失败次数
	 * @return 成功/失败
	 */
	Boolean postRestTemplate(String notifyUrl, Object jsonParams, int maxFailure);

	/**
	 * 使用RestTemplate POST方式进行回调
	 * 
	 * @param notifyUrl
	 *            回调地址
	 * @param jsonParams
	 *            可转化为JSON的参数对象
	 * @param increaseSeconds
	 *            递增秒数
	 * @param maxFailure
	 *            最大失败次数
	 * @return 成功/失败
	 */
	Boolean postRestTemplate(String notifyUrl, Object jsonParams, int increaseSeconds, int maxFailure);
	
	/**
	 * 使用RestTemplate POST方式进行回调
	 * 
	 * @param notifyUrl
	 *            回调地址
	 * @param jsonParams
	 *            可转化为JSON的参数对象
	 * @param increaseSeconds
	 *            递增秒数
	 * @param maxFailure
	 *            最大失败次数
	 * @param secondsStrategy
	 *            递增秒数策略
	 * @return 成功/失败
	 */
	Boolean postRestTemplate(String notifyUrl, Object jsonParams, int increaseSeconds, int maxFailure, InnerCallbackEnum.SecondsStrategy secondsStrategy);

	/**
	 * 使用RestTemplate POST方式进行回调
	 * 
	 * @param notifyUrl
	 *            回调地址
	 * @param jsonParams
	 *            可转化为JSON的参数对象
	 * @param processorBeanName
	 *            处理器bean名称
	 * @return 成功/失败
	 */
	Boolean postRestTemplate(String notifyUrl, Object jsonParams, ProcessorBeanName processorBeanName);
	
	/**
	 * 使用RestTemplate POST方式进行回调
	 * 
	 * @param notifyUrl
	 *            回调地址
	 * @param jsonParams
	 *            可转化为JSON的参数对象
	 * @param processorBeanName
	 *            处理器bean名称
	 * @param maxFailure
	 *            最大失败次数
	 * @return 成功/失败
	 */
	Boolean postRestTemplate(String notifyUrl, Object jsonParams, ProcessorBeanName processorBeanName, int maxFailure);

	/**
	 * 使用RestTemplate POST方式进行回调
	 * 
	 * @param notifyUrl
	 *            回调地址
	 * @param jsonParams
	 *            可转化为JSON的参数对象
	 * @param processorBeanName
	 *            处理器bean名称
	 * @param increaseSeconds
	 *            递增秒数
	 * @param maxFailure
	 *            最大失败次数
	 * @param secondsStrategy
	 *            递增秒数策略
	 * @return 成功/失败
	 */
	Boolean postRestTemplate(String notifyUrl, Object jsonParams, ProcessorBeanName processorBeanName,
			int increaseSeconds, int maxFailure, InnerCallbackEnum.SecondsStrategy secondsStrategy);
	
	/**
	 * 使用RestTemplate POST方式进行回调
	 * 
	 * @param notifyUrl
	 *            回调地址
	 * @param jsonParams
	 *            可转化为JSON的参数对象
	 * @param processorBeanName
	 *            处理器bean名称
	 * @param increaseSeconds
	 *            递增秒数
	 * @param maxFailure
	 *            最大失败次数
	 * @param secondsStrategy
	 *            递增秒数策略
	 * @param nextDisposeTime
	 *            指定首次的下次执行时间，可用于首次重试延迟
	 * @return 成功/失败
	 */
	Boolean postRestTemplate(String notifyUrl, Object jsonParams, ProcessorBeanName processorBeanName,
			int increaseSeconds, int maxFailure, InnerCallbackEnum.SecondsStrategy secondsStrategy, Date nextDisposeTime);

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
