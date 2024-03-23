package org.sici.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: oa-parent
 * @package: org.sici.activiti
 * @className: GatewayTest
 * @author: 749291
 * @description: TODO
 * @date: 3/21/2024 7:18 PM
 * @version: 1.0
 */

@SpringBootTest
public class GatewayTest {
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
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("process/shenpi.bpmn20.xml")
                .name("审批流程")
                .key("shenpi")
                .deploy();

        System.out.println(deploy.getName());
    }

    @Test
    void findPendingTask() {
        taskService.createTaskQuery().taskAssignee("lisi").list().forEach(task -> {
            System.out.println(task.getName());
            System.out.println(task.getAssignee());
        });
    }
    @Test
    void startProcess() {
        Map<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("day", 5);
        ProcessInstance qingjia = runtimeService.startProcessInstanceByKey("shenpi",
                stringObjectHashMap);
        System.out.println(qingjia.getProcessDefinitionId());
//        System.out.println(qingjia.getActivityId());
        findPendingTask();
    }
}
