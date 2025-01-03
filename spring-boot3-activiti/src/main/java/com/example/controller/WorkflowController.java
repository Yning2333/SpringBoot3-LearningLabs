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

    /**
     * URL_ADDRESS
     * POST http://127.0.0.1:8080/workflow/start/{processKey}
     *
     * 启动一个流程实例
     * @param processKey 流程定义的 key
     * @return 启动的流程实例 ID
     */
    @PostMapping("/start/{processKey}")
    public String startProcess(@PathVariable String processKey) {
        String processInstanceId = workflowService.startProcess(processKey);
        return "Process started successfully! Instance ID: " + processInstanceId;
    }

    /**
     * URL_ADDRESS
     * GET http://127.0.0.1:8080/workflow/definitions
     *
     * 获取所有流程定义
     * @return 流程定义列表，每个定义包含 ID、名称、Key 等信息
     */
    @GetMapping("/definitions")
    public List<Map<String, Object>> getDefinitions() {
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery().list();
        return workflowService.convertProcessDefinitions(definitions);
    }

    /**
     * URL_ADDRESS
     * GET http://127.0.0.1:8080/workflow/instances
     *
     * 获取所有流程实例及对应的任务 ID
     * @return Map，Key 为流程实例 ID，Value 为任务 ID
     */
    @GetMapping("/instances")
    public Map<String, String> getProcessInstances() {
        return workflowService.getAllProcessInstancesWithTaskId();
    }

    /**
     * URL_ADDRESS
     * GET http://127.0.0.1:8080/workflow/tasks/{instanceId}
     *
     * 根据流程实例 ID 获取任务列表
     * @param instanceId 流程实例 ID
     * @return 任务列表
     */
    @GetMapping("/tasks/{instanceId}")
    public List<String> getTasksByInstanceId(@PathVariable String instanceId) {
        return workflowService.getTasksByInstanceId(instanceId);
    }

    /**
     * URL_ADDRESS
     * POST http://127.0.0.1:8080/workflow/tasks/{taskId}/complete?approved=true
     *
     * 完成任务，并传入变量 approved
     * @param taskId 任务 ID
     * @param approved 是否批准，默认为 false
     * @return 操作结果字符串
     */
    @PostMapping("/tasks/{taskId}/complete")
    public String completeTask(@PathVariable String taskId, @RequestParam(required = false) Boolean approved) {
        // 构造传递给任务完成的变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", approved != null ? approved : false);  // 如果未传入 approved，默认为 false

        workflowService.completeTask(taskId, variables);

        return "Task completed successfully!";
    }

    /**
     * URL_ADDRESS
     * GET http://127.0.0.1:8080/workflow/history/tasks
     *
     * 查询历史任务记录
     * @return 历史任务列表，每条任务包含 ID、名称等信息
     */
    @GetMapping("/history/tasks")
    public List<Map<String, Object>> getHistoricTasks() {
        return workflowService.getHistoricTasks();
    }

    /**
     * URL_ADDRESS
     * GET http://127.0.0.1:8080/workflow/history/processes
     *
     * 查询历史流程实例记录
     * @return 历史流程实例列表，每个实例包含 ID、定义 Key 等信息
     */
    @GetMapping("/history/processes")
    public List<Map<String, Object>> getHistoricProcessInstances() {
        return workflowService.getHistoricProcessInstances();
    }
}
