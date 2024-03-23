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

/**
 * @projectName: oa-parent
 * @package: org.sici.activiti
 * @className: TaskGroupTest
 * @author: 749291
 * @description: TODO
 * @date: 3/21/2024 6:35 PM
 * @version: 1.0
 */

@SpringBootTest
public class TaskGroupTest {
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
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("process/jiaban04.bpmn20.xml")
                .name("加班流程")
                .key("jiaban04")
                .deploy();

        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }
    @Test
    void startProcess() {
        ProcessInstance qingjia = runtimeService.startProcessInstanceByKey("jiaban04");
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
    void findGroupTask() {
        taskService.createTaskQuery().taskCandidateUser("rose02").list().forEach(task -> {
            System.out.println(task.getName());
            System.out.println(task.getAssignee());
            System.out.println(task.getProcessDefinitionId());
        });
    }

    @Test
    void claimTask() {
        taskService.createTaskQuery().taskCandidateUser("tom01").list().forEach(task -> {
            taskService.claim(task.getId(), "tom01");
            System.out.println("tom01 任务拾取成功: " + task.getName() + ": " + task.getProcessDefinitionId());
        });
    }

    @Test
    void compeleteTask() {
        taskService.createTaskQuery().taskAssignee("tom01").list().forEach(task -> {
            taskService.complete(task.getId());
            System.out.println("tom01 任务完成: " + task.getName() + ": " + task.getProcessDefinitionId());
        });
    }

    @Test
    void assigneeToGroup() {
        Task task = taskService.createTaskQuery().taskAssignee("tom01").singleResult();
        if (task != null) {
            taskService.setAssignee(task.getId(), null);
//            taskService.addCandidateGroup(task.getId(), "group1");
        }
    }

    @Test
    void assigneeToCandidateUser() {
        Task task = taskService.createTaskQuery().taskAssignee("tom01").singleResult();
        if (task != null) {
            taskService.addCandidateUser(task.getId(), "tom02");
        }
    }
}

