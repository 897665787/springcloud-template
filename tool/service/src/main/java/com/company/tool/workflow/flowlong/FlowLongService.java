package com.company.tool.workflow.flowlong;

import com.aizuda.bpm.engine.FlowLongEngine;
import com.aizuda.bpm.engine.core.FlowCreator;
import com.aizuda.bpm.engine.entity.FlwTask;
import com.company.tool.workflow.WorkFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class FlowLongService implements WorkFlowService {
    @Resource
    private FlowLongEngine flowLongEngine;

    @Override
    public Long deployProcess(String flowJson, String creator) {
        FlowCreator flowCreator = FlowCreator.of(creator, creator);
        return flowLongEngine.processService().deploy(null, flowJson, flowCreator, false, null);
    }

    @Override
    public Long startInstance(Long processId, String businessKey, String starter) {
        FlowCreator flowCreator = FlowCreator.of(starter, starter);
        return flowLongEngine.startInstanceById(processId, flowCreator, businessKey).map(instance -> instance.getId()).orElse(null);
    }

    @Override
    public void completeTask(Long taskId, String userId, String comment, Boolean approved) {
        FlowCreator flowCreator = FlowCreator.of(userId, userId);
        flowLongEngine.taskService().complete(taskId, flowCreator, comment, approved, null);
    }

    @Override
    public List<FlwTask> getTodoTasks(String userId, Integer pageNum, Integer pageSize) {
        return flowLongEngine.queryService().getActiveTasks(userId, pageNum, pageSize);
    }

    @Override
    public List<FlwTask> getDoneTasks(String userId, Integer pageNum, Integer pageSize) {
        return flowLongEngine.queryService().getDoneTasks(userId, pageNum, pageSize);
    }

    @Override
    public List<FlwTask> getCcTasks(String userId, Integer pageNum, Integer pageSize) {
        return flowLongEngine.queryService().getCcTasks(userId, pageNum, pageSize);
    }

    @Override
    public List<FlwTask> getStartedTasks(String userId, Integer pageNum, Integer pageSize) {
        return flowLongEngine.queryService().getApplyTasks(userId, pageNum, pageSize);
    }
}