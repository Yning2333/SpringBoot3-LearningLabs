package com.example.service;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkflowService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    // 启动流程
    public String startProcess(String processKey) {
        return runtimeService.startProcessInstanceByKey(processKey).getId();
    }

    // 获取所有流程定义
    public List<String> getAllProcessDefinitions() {
        return repositoryService.createProcessDefinitionQuery()
                .list()
                .stream()
                .map(def -> "ID: " + def.getId() + ", Name: " + def.getName())
                .collect(Collectors.toList());
    }

    // 将 ProcessDefinition 转换为可视化的数据
    // 转换流程定义为 Map<String, Object>
    public List<Map<String, Object>> convertProcessDefinitions(List<ProcessDefinition> definitions) {
        return definitions.stream().map(def -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", def.getId());
            map.put("name", def.getName());
            map.put("key", def.getKey());
            map.put("version", def.getVersion());
            return map;
        }).collect(Collectors.toList());
    }

    // 获取所有流程实例及其对应的任务ID，返回 Map
    public Map<String, String> getAllProcessInstancesWithTaskId() {
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().list();

        Map<String, String> result = new HashMap<>();
        for (ProcessInstance instance : processInstances) {
            // 获取流程实例的 ID
            String instanceId = instance.getId();

            // 获取流程实例的当前任务
            Task task = taskService.createTaskQuery()
                    .processInstanceId(instanceId)
                    .singleResult(); // 只取一个任务，假设每个实例只有一个任务

            if (task != null) {
                // 将实例ID与任务ID加入 Map
                result.put(instanceId, task.getId());
            }
        }

        return result;
    }

    // 根据流程实例ID获取任务
    public List<String> getTasksByInstanceId(String instanceId) {
        return taskService.createTaskQuery()
                .processInstanceId(instanceId)
                .list()
                .stream()
                .map(Task::getName)
                .collect(Collectors.toList());
    }

    // 完成任务
    public void completeTask(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId,variables);
    }

    // 查询历史任务记录
    public List<Map<String, Object>> getHistoricTasks() {
        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().desc() // 按结束时间降序排序
                .list();

        return historicTasks.stream()
                .map(task -> {
                    Map<String, Object> taskInfo = new HashMap<>();
                    taskInfo.put("taskId", task.getId());
                    taskInfo.put("processInstanceId", task.getProcessInstanceId());
                    taskInfo.put("processDefinitionId", task.getProcessDefinitionId());
                    taskInfo.put("taskName", task.getName());
                    taskInfo.put("assignee", task.getAssignee());
                    taskInfo.put("startTime", task.getStartTime());
                    taskInfo.put("endTime", task.getEndTime());
                    return taskInfo;
                })
                .collect(Collectors.toList());
    }

    // 查询历史流程实例记录
    public List<Map<String, Object>> getHistoricProcessInstances() {
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
                .orderByProcessInstanceEndTime().desc()  // 按结束时间降序排序
                .list();

        return historicProcessInstances.stream()
                .map(instance -> {
                    Map<String, Object> instanceInfo = new HashMap<>();
                    instanceInfo.put("processInstanceId", instance.getId());
                    instanceInfo.put("processDefinitionId", instance.getProcessDefinitionId());
                    instanceInfo.put("startTime", instance.getStartTime());
                    instanceInfo.put("endTime", instance.getEndTime());
                    // 判断状态：如果 endTime 不为 null，则为 "COMPLETED"，否则为 "RUNNING"
                    String status = (instance.getEndTime() != null) ? "COMPLETED" : "RUNNING";
                    instanceInfo.put("status", status);
                    return instanceInfo;
                })
                .collect(Collectors.toList());
    }
}
