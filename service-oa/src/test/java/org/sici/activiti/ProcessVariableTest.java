package org.sici.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: oa-parent
 * @package: org.sici.activiti
 * @className: ProcessVariableTest
 * @author: 749291
 * @description: TODO
 * @date: 3/21/2024 6:23 PM
 * @version: 1.0
 */

@SpringBootTest
public class ProcessVariableTest {
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
        Map<String, Object> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("assignee1", "suxiaowen");
//        stringStringHashMap.put("assignee2", "pengjianeng");

        ProcessInstance qingjia = runtimeService.startProcessInstanceByKey("jiaban", stringStringHashMap);
        System.out.println(qingjia.getProcessDefinitionId());
        System.out.println(qingjia.getId());
        System.out.println(qingjia.getActivityId());
    }
    @Test
    void findPendingTask() {
        taskService.createTaskQuery().taskAssignee("zhaowu").list().forEach(task -> {
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getAssignee());
        });
    }
    @Test
    void compeleteTask() {
        Task task = taskService.createTaskQuery().taskAssignee("suxiaowen").singleResult();

        HashMap<String, Object> variable = new HashMap<>();

        variable.put("assignee2", "zhaowu");

        taskService.complete(task.getId(), variable);
    }


    @Test
    void testSetVariable() {
        // runtimeService.setVariable(executionId, variableName, value);

    }
}

