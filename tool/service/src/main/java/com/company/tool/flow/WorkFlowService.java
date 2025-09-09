package com.company.tool.flow;

import com.aizuda.bpm.engine.entity.FlwTask;

import java.util.List;

public interface WorkFlowService {

    /**
     * 部署流程
     */
    Long deployProcess(String flowJson, String creator);

    /**
     * 发起申请
     */
    Long startInstance(Long processId, String businessKey, String starter);

    /**
     * 审批（通过或驳回）
     */
    void completeTask(Long taskId, String userId, String comment, Boolean approved);

    /**
     * 任务列表-待办
     */
    List<FlwTask> getTodoTasks(String userId, Integer pageNum, Integer pageSize);
    /**
     * 任务列表-已办
     */
    List<FlwTask> getDoneTasks( String userId, Integer pageNum, Integer pageSize);
    /**
     * 任务列表-抄送我
     */
    List<FlwTask> getCcTasks( String userId, Integer pageNum, Integer pageSize);
    /**
     * 任务列表-已发起
     */
    List<FlwTask> getStartedTasks( String userId, Integer pageNum, Integer pageSize);
}
