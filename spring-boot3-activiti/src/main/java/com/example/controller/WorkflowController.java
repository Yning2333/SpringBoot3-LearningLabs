package com.example.controller;

import com.example.service.WorkflowService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workflow")
public class WorkflowController {

    // 使用 @Autowired 注解进行字段注入
    @Autowired
    private WorkflowService workflowService;

    // 也可以注入其他服务，例如 RepositoryService
    @Autowired
    private RepositoryService repositoryService;

    // 启动一个流程
    @PostMapping("/start/{processKey}")
    public String startProcess(@PathVariable String processKey) {
        String processInstanceId = workflowService.startProcess(processKey);
        return "Process started successfully! Instance ID: " + processInstanceId;
    }

    // 获取所有流程定义
    @GetMapping("/definitions")
    public List<Map<String, Object>> getDefinitions() {
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery().list();
        return workflowService.convertProcessDefinitions(definitions);
    }

    // 获取所有流程实例
    @GetMapping("/instances")
    public Map<String, String> getProcessInstances() {
        return workflowService.getAllProcessInstancesWithTaskId();
    }


    // 根据流程实例ID获取任务
    @GetMapping("/tasks/{instanceId}")
    public List<String> getTasksByInstanceId(@PathVariable String instanceId) {
        return workflowService.getTasksByInstanceId(instanceId);
    }

    // 完成任务,并传入 approved 变量
    @PostMapping("/tasks/{taskId}/complete")
    public String completeTask(@PathVariable String taskId, @RequestParam(required = false) Boolean approved) {
        // 构造传递给任务完成的变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", approved != null ? approved : false);  // 如果未传入 approved，默认为 false

        workflowService.completeTask(taskId, variables);

        return "Task completed successfully!";
    }

    // 查询历史任务记录
    @GetMapping("/history/tasks")
    public List<Map<String, Object>> getHistoricTasks() {
        return workflowService.getHistoricTasks();
    }

    // 查询历史流程实例记录
    @GetMapping("/history/processes")
    public List<Map<String, Object>> getHistoricProcessInstances() {
        return workflowService.getHistoricProcessInstances();
    }
}
