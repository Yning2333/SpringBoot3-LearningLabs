package com.example.config;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentQuery;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlowableDeploymentService {

    @Autowired
    private RepositoryService repositoryService;

    @Value("${flowable.bpmn.folder}")
    private String bpmnFolder;

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

    /**
     * 检查是否已经存在相同的流程定义
     * @param processId
     * @return
     */

    private boolean isProcessAlreadyDeployed(String processId) {
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        return deploymentQuery.processDefinitionKey(processId).count() > 0;
    }



    private void deployProcess(Resource resource) {
        // 读取 BPMN 文件中的 process id
        try {
            String processId = getProcessDefinitionId(resource.getInputStream());
            if (isProcessAlreadyDeployed(processId)) {
                System.out.println("流程 " + processId + " 已经部署，跳过部署。");
                return; // 如果流程已经部署，则不再重复部署
            }
            String resourceName = resource.getFilename();
            // 执行部署
            Deployment deployment = repositoryService.createDeployment()
                    .addInputStream(resourceName, resource.getInputStream())
                    .name("自动部署：" + resourceName)
                    .deploy();

            System.out.println("流程部署成功：");
            System.out.println("部署 ID：" + deployment.getId());
            System.out.println("部署名称：" + deployment.getName());
        } catch (IOException e) {
            System.err.println("部署流程失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    // 解析 BPMN 文件中的 process id
    public static String getProcessDefinitionId(InputStream inputStream) {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(inputStream);
            return document.getRootElement()
                    .getChild("process", document.getRootElement().getNamespace())
                    .getAttributeValue("id");
        } catch (Exception e) {
            throw new RuntimeException("Error reading process definition ID from XML", e);
        }
    }

}
