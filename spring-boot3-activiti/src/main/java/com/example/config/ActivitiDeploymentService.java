package com.example.config;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivitiDeploymentService {

    @Value("${spring.activiti.bpmn.folder}")
    private String bpmnFolder;

    @Autowired
    private RepositoryService repositoryService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() throws IOException {
        deployProcesses();
    }

    public void deployProcesses() throws IOException {
        List<Resource> resources = getBpmnFilesFromClasspath();

        for (Resource resource : resources) {
            deployProcess(resource);
        }
    }

    // 获取所有BPMN文件
    private List<Resource> getBpmnFilesFromClasspath() throws IOException {
        // 假设所有BPMN文件存储在 src/main/resources/processes 目录下
        ClassPathResource folderResource = new ClassPathResource(bpmnFolder);

        // 确保目录存在并读取所有文件
        if (folderResource.exists()) {
            return List.of(folderResource.getFile().listFiles()) // 获取文件列表
                    .stream()
                    .filter(file -> file.getName().endsWith(".bpmn"))
                    .map(file -> new ClassPathResource(bpmnFolder + file.getName()))
                    .collect(Collectors.toList());
        }
        return List.of(); // 如果没有文件，则返回空列表
    }

    // 部署单个BPMN文件
    public void deployProcess(Resource resource) {
        try {
            if (resource.exists()) {
                // 读取 BPMN 文件中的 process id
                String processId = getProcessIdFromBpmn(resource.getInputStream());

                if (processId != null) {
                    // 检查是否已经存在相同的流程定义
                    ProcessDefinition existingProcess = repositoryService.createProcessDefinitionQuery()
                            .processDefinitionKey(processId) // 根据 processId 查找
                            .latestVersion()
                            .singleResult();

                    // 如果没有找到已有的流程定义，则进行部署
                    if (existingProcess == null) {
                        Deployment deployment = repositoryService.createDeployment()
                                .addInputStream(resource.getFilename(), resource.getInputStream()) // 将文件内容添加到部署
                                .name(resource.getFilename())
                                .deploy();
                        System.out.println("Deployed: " + deployment.getId());
                    } else {
                        System.out.println("Process already deployed: " + existingProcess.getId());
                    }
                } else {
                    System.out.println("Unable to find process id in the BPMN file: " + resource.getFilename());
                }
            } else {
                System.out.println("Process file not found: " + resource.getFilename());
            }
        } catch (IOException e) {
            System.out.println("Error deploying process: " + e.getMessage());
        }
    }

    // 解析 BPMN 文件中的 process id
    private String getProcessIdFromBpmn(InputStream inputStream) {
        try {
            // 解析 BPMN 文件，获取 <process> 标签的 id 属性
            XmlNode bpmnDocument = XmlUtils.parseXml(inputStream);
            XmlNode processNode = bpmnDocument.getElementsByTagName("process").item(0);
            if (processNode != null) {
                return processNode.getAttributes().getNamedItem("id").getTextContent();
            }
        } catch (Exception e) {
            System.out.println("Error reading BPMN file: " + e.getMessage());
        }
        return null;
    }
}
