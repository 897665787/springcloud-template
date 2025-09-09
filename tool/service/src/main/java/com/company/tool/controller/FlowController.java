package com.company.tool.controller;

import com.aizuda.bpm.engine.FlowLongEngine;
import com.aizuda.bpm.engine.core.FlowCreator;
import com.aizuda.bpm.engine.entity.FlwInstance;
import com.aizuda.bpm.engine.entity.FlwTask;
import com.company.common.api.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/flow")
public class FlowController {

	@Autowired
	private FlowLongEngine flowLongEngine;

	@PostMapping(value = "/deployProcess")
	public Result<Object> deployProcess(@RequestBody String jsonString) {
		// 返回 流程定义ID 用于后续发起流程实例
		FlowCreator flowCreator = FlowCreator.of("11", "zhangsan");
		Long processId = flowLongEngine.processService().deploy(null, jsonString, flowCreator, false, null);
		return Result.success(processId);
	}

	@RequestMapping(value = "/startProcessInstance")
	public Result<Object> startProcessInstance(Long processId) {
		// 设置流程变量（可选）
		Map<String, Object> variables = new HashMap<>();
		String businessKey = "qingjia";
		variables.put("businessKey", businessKey);
		String starter = "lisi";
		variables.put("starter", starter);
		variables.put("leaderId", "21"); // 用于动态指定审批人

		FlowCreator flowCreator = FlowCreator.of("12", starter);
		// 启动流程实例
		Optional<FlwInstance> flwInstance = flowLongEngine.startInstanceById(processId, flowCreator, variables);
		FlwInstance flwInstance1 = flwInstance.orElse(null);
		return Result.success(flwInstance1);
	}

	@RequestMapping(value = "/getActiveTasks")
	public Result<Object> getActiveTasks(Long instanceId) {

		// 审核通过
		FlowCreator flowCreator = FlowCreator.of("12", "lisi");
		boolean b = flowLongEngine.executeTask(1L, flowCreator);

		FlwTask currentFlwTask = flowLongEngine.queryService().getTask(2L);
		flowLongEngine.executeRejectTask(currentFlwTask, flowCreator);

		List<FlwTask> activeTasks = flowLongEngine.queryService().getTasksByInstanceId(instanceId);
		return Result.success(activeTasks);
	}

}
