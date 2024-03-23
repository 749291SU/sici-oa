package org.sici.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: oa-parent
 * @package: org.sici.activiti
 * @className: ActivitiTest
 * @author: 749291
 * @description: TODO
 * @date: 3/17/2024 3:25 PM
 * @version: 1.0
 */

@SpringBootTest
public class ActivitiTest {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    @Test
    void deployTest() {
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("process/qingjia.bpmn20.xml")
                .addClasspathResource("process/qingjia.png")
                .name("请假流程")
                .key("qingjia")
                .deploy();

        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }


    @Test
    void startProcess() {
        ProcessInstance qingjia = runtimeService.startProcessInstanceByKey("qingjia");
        System.out.println(qingjia.getProcessDefinitionId());
        System.out.println(qingjia.getId());
        System.out.println(qingjia.getActivityId());
    }

    @Test
    void findPendingTask() {
        taskService.createTaskQuery().taskAssignee("zhangsan").list().forEach(task -> {
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getAssignee());
        });
    }

    @Test
    void dealTask() {
         taskService.createTaskQuery().taskAssignee("zhangsan").list().forEach(task -> {
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getAssignee());
            taskService.complete(task.getId());
        });
    }

    // 查询已处理的任务
    @Test
    void findHistoryTask() {
        historyService.createHistoricTaskInstanceQuery().taskAssignee("zhangsan").list().forEach(task -> {
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getAssignee());
        });
    }

    @Test
    void startProcessByKey() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qingjia", "1001");
    }


    // suspendProcess
    @Test
    void suspendProcess() {
        repositoryService.createProcessDefinitionQuery().processDefinitionKey("qingjia").list().forEach(qingjia -> {
            System.out.println(qingjia);
            Boolean isSuspended = qingjia.isSuspended();

            if (isSuspended) {
                repositoryService.activateProcessDefinitionById(qingjia.getId(), true, null);
                System.out.println("激活成功: " + qingjia.getName());
            } else {
                repositoryService.suspendProcessDefinitionById(qingjia.getId());
                System.out.println("挂起成功: " + qingjia.getName());
            }
        });
    }

    // UEL
    @Test
    void deployUEL() {
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("process/jiaban.bpmn20.xml")
                .name("加班")
                .key("jiaban")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    @Test
    void startProcessUEL() {
        Map<String, Object> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("assignee1", "suxiaowen");
        stringStringHashMap.put("assignee2", "pengjianeng");

        ProcessInstance jiaban = runtimeService.startProcessInstanceByKey("jiaban", "1001",
        stringStringHashMap);

        System.out.println(jiaban.getProcessDefinitionId());
        System.out.println(jiaban.getId());
        System.out.println(jiaban.getActivityId());
    }

    @Test
    void findPendingTask1() {
        taskService.createTaskQuery().taskAssignee("Jack").list().forEach(task -> {
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getAssignee());
        });
    }

    // listener
    @Test
    void deployListener() {
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("process/jiaban01.bpmn20.xml")
                .name("加班")
                .key("jiaban01")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    @Test
    void startProcessListener() {
        ProcessInstance jiaban = runtimeService.startProcessInstanceByKey("jiaban01");
        System.out.println(jiaban.getProcessDefinitionId());
        System.out.println(jiaban.getId());
        System.out.println(jiaban.getActivityId());
    }
}
