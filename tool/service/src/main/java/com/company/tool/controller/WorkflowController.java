package com.company.tool.controller;

import com.aizuda.bpm.engine.FlowLongEngine;
import com.aizuda.bpm.engine.core.FlowCreator;
import com.aizuda.bpm.engine.entity.FlwInstance;
import com.aizuda.bpm.engine.entity.FlwProcess;
import com.aizuda.bpm.engine.entity.FlwTask;
import com.company.common.api.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/workflow")
public class WorkflowController {

	@Autowired
	private FlowLongEngine flowLongEngine;

	/**
	 * 部署流程
	 * @param jsonString
	 * @return
	 */
	@PostMapping(value = "/deployProcess")
	public Result<Object> deployProcess(@RequestBody String jsonString) {
		// 返回 流程定义ID 用于后续发起流程实例
		FlowCreator flowCreator = FlowCreator.of("11", "zhangsan");
		Long processId = flowLongEngine.processService().deploy(null, jsonString, flowCreator, false, null);
		return Result.success(processId);
	}

	/**
	 * 获取已部署且启用的流程
	 * @return
	 */
	@GetMapping(value = "/listProcess")
	public Result<List<FlwProcess>> listProcess() {
		return Result.success();
	}

	/**
	 * 下架流程
	 *
	 * @return
	 */
	@PostMapping(value = "/inactive")
	public Result<List<FlwProcess>> inactive(Long processId) {
		boolean success = flowLongEngine.processService().undeploy(processId);
		return Result.success();
	}

	/*
	 * 发起流程申请
	 */
	@PostMapping(value = "/apply")
	public Result<Object> apply(Long processId) {
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

	/*
	 * 审批任务-通过
	 */
	@PostMapping(value = "/pass")
	public Result<Object> pass(Long taskId) {
		FlowCreator flowCreator = FlowCreator.of("12", "app");
		boolean b = flowLongEngine.executeTask(taskId, flowCreator);
		return Result.success();
	}

	/*
	 * 审批任务-驳回
	 */
	@PostMapping(value = "/reject")
	public Result<Object> reject(Long taskId) {
		FlowCreator flowCreator = FlowCreator.of("12", "app");

		FlwTask currentFlwTask = flowLongEngine.queryService().getTask(taskId);
		Optional<List<FlwTask>> flwTasks = flowLongEngine.executeRejectTask(currentFlwTask, flowCreator);

		return Result.success();
	}

	/*
	 * 审批任务-转办
	 */
	@PostMapping(value = "/transfer")
	public Result<Object> transfer(Long taskId) {
		FlowCreator flowCreator = FlowCreator.of("12", "app");
		FlowCreator assigneeFlowCreator = FlowCreator.of("21", "lisi");

		boolean b = flowLongEngine.taskService().transferTask(taskId, flowCreator, assigneeFlowCreator);

		return Result.success();
	}

	/**
	 * 获取待办任务列表
	 * @param instanceId
	 * @return
	 */
	@GetMapping(value = "/listToDoTask")
	public Result<Object> listToDoTask(Long instanceId) {

		// 审核通过
		FlowCreator flowCreator = FlowCreator.of("12", "lisi");
		boolean b = flowLongEngine.executeTask(1L, flowCreator);

		List<FlwTask> activeTasks1 = flowLongEngine.queryService().getActiveTasks(2L, null);

		List<FlwTask> activeTasks = flowLongEngine.queryService().getTasksByInstanceId(instanceId);
		return Result.success(activeTasks);
	}

}
