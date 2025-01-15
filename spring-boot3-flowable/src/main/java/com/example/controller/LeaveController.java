package com.example.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    /**
     * 显示流程图
     *
     * @param resp
     * @param processId 流程实例ID
     * @throws Exception
     */
    @GetMapping("/pic")
    public void showPic(HttpServletResponse resp, String processId) throws Exception {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        if (pi == null) {
            return;
        }
        List<Execution> executions = runtimeService
                .createExecutionQuery()
                .processInstanceId(processId)
                .list();

        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        /**
         * 生成流程图
         */
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows, engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(), engconf.getClassLoader(), 1.0, false);
        OutputStream out = null;
        byte[] buf = new byte[1024];
        int legth = 0;
        try {
            out = resp.getOutputStream();
            while ((legth = in.read(buf)) != -1) {
                out.write(buf, 0, legth);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }


    /**
     * 开启请假流程
     *
     * @param employeeId 员工ID
     * @param days       请假天数
     * @param reason     请假原因
     * @return 流程实例ID
     */
    @PostMapping("/start")
    public String startLeaveProcess(@RequestParam String employeeId,
                                    @RequestParam int days,
                                    @RequestParam String reason) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("employeeId", employeeId);
        variables.put("days", days);
        variables.put("reason", reason);

        variables.put("leaveTask", employeeId);

        // 开启请假流程
        var processInstance = runtimeService.startProcessInstanceByKey("ask_for_leave", variables);
        return "流程已启动，实例ID：" + processInstance.getId();
    }

    /**
     * 获取任务列表
     *
     * @param employeeId 员工ID
     * @return 任务列表
     */
    @GetMapping("/tasks")
    public List<String> getTasks(@RequestParam String employeeId) {
        List<String> taskList = new ArrayList<>();
        List<Task> list = taskService.createTaskQuery().taskAssignee(employeeId).orderByTaskId().desc().list();
        for (Task task : list) {
            taskList.add("任务ID：" + task.getId() + ", 任务名称：" + task.getName() + ", 任务办理人：" + task.getAssignee());
        }
        return taskList;
    }

    /**
     * 员工提交请假
     *
     * @param taskId 任务ID
     * @return 提交结果
     */
    @PostMapping("/submit")
    public String submitLeave(@RequestParam String taskId, @RequestParam String zuzhangId) {
        Map<String, Object> map = new HashMap<>();
        //提交给组长的时候，需要指定组长的 id
        map.put("zuzhangTask", zuzhangId);
        taskService.complete(taskId, map);
        return "请假申请已提交";
    }

    /**
     * 组长批准请假
     *
     * @param zuzhangId      组长ID
     * @param manageId       经理ID
     * @param checkResult    审批结果
     * @param taskId         任务ID
     * @return 审批结果
     */
    @PostMapping("/leaderApprove")
    public String leaderApprove(@RequestParam String zuzhangId,
                                @RequestParam String manageId,
                                @RequestParam String checkResult,
                                @RequestParam String taskId) {
        // 创建一个映射，用于存储任务相关的变量
        Map<String, Object> variables = new HashMap<>();
        // 提交给组长的时候，需要指定组长的 id
        variables.put("manageTask", manageId);
        // 存储审批结果
        variables.put("checkResult", checkResult);
        // 指定当前任务处理人是组长
        variables.put("zuzhangTask", zuzhangId);
        // 完成任务并传递相关变量
        taskService.complete(taskId, variables);
        // 根据审批结果返回不同的消息
        return "通过".equals(checkResult) ? "组长批准请假" : "组长拒绝请假";
    }

    /**
     * 经理审批
     *
     * @param taskId   任务ID
     * @param checkResult 审批结果
     * @return 审批结果
     */
    @PostMapping("/managerApprove")
    public String managerApprove(@RequestParam String taskId, @RequestParam String checkResult) {
        Map<String, Object> map = new HashMap<>();
        map.put("checkResult", checkResult);
        taskService.complete(taskId, map);
        return "通过".equals(checkResult) ? "经理批准请假" : "经理拒绝请假";
    }

    /**
     * 查询历史任务
     *
     * 该接口用于获取指定员工的历史任务信息
     * 它通过员工ID过滤历史任务，并按任务结束时间降序排序
     *
     * @param employeeId 员工ID，用于过滤历史任务
     * @return 返回一个包含历史任务信息的列表，每个任务的信息以键值对形式表示
     */
    @GetMapping("/history/tasks")
    public List<Map<String, Object>> getHistoricTasks(@RequestParam String employeeId) {
        // 查询历史任务实例，按指定员工ID过滤
        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(employeeId)
                .orderByHistoricTaskInstanceEndTime().desc()
                .list();

        // 将历史任务实例转换为包含任务信息的Map列表
        return historicTasks.stream()
                .map(task -> {
                    Map<String, Object> taskInfo = new HashMap<>();
                    taskInfo.put("taskId", task.getId());
                    taskInfo.put("taskName", task.getName());
                    taskInfo.put("assignee", task.getAssignee());
                    taskInfo.put("startTime", task.getStartTime());
                    taskInfo.put("endTime", task.getEndTime());
                    return taskInfo;
                })
                .collect(Collectors.toList());
    }
}