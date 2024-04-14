package org.sici.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.sici.custom.LoginUserInfoHelper;
import org.sici.model.process.Process;
import org.sici.model.process.ProcessRecord;
import org.sici.model.process.ProcessTemplate;
import org.sici.model.process.ProcessType;
import org.sici.model.system.SysUser;
import org.sici.result.Result;
import org.sici.service.*;
import org.sici.mapper.ProcessMapper;
import org.sici.vo.process.ApprovalVo;
import org.sici.vo.process.ProcessFormVo;
import org.sici.vo.process.ProcessQueryVo;
import org.sici.vo.process.ProcessVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
* @author 20148
* @description 针对表【oa_process(审批类型)】的数据库操作Service实现
* @createDate 2024-01-30 19:45:08
*/
@Service
@Slf4j
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process>
    implements ProcessService {
    @Autowired
    private ProcessMapper processMapper;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessTemplateService processTemplateService;
    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessRecordService processRecordService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private MessageService messageService;

    @Override
    public IPage<ProcessVo> selectPage(IPage<ProcessVo> pagePram, ProcessQueryVo processQueryVo) {
        IPage<ProcessVo> processVoIPage = processMapper.selectPageVo(pagePram, processQueryVo);
        return processVoIPage;
    }

    @Override
    public void deployProcessByZip(String zipFilePath) {
        // 通过压缩包文件部署流程定义
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(zipFilePath);

        ZipInputStream zipInputStream = new ZipInputStream(resourceAsStream);

        Deployment deploy = repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
        log.info("---------deploy process success, deploy id: {}, deploy name:{}--------------", deploy.getId(), deploy.getName());
    }

    @Override
    public Result findProcessType() {
        List<ProcessType> allTypes = processTypeService.list();
        for (ProcessType processType : allTypes) {
            LambdaQueryWrapper<ProcessTemplate> query = new LambdaQueryWrapper<>();
            query.eq(ProcessTemplate::getProcessTypeId, processType.getId());
            processType.setProcessTemplateList(processTemplateService.list(query));
        }
        return Result.ok(allTypes);
    }

    @Override
    public Result startUp(ProcessFormVo processFormVo) {
        //get userId(Long)
        Long userId = LoginUserInfoHelper.getUserId();
        SysUser sysUser = sysUserService.getById(userId);

        //get processTemplate by id
        Long processTemplateId = processFormVo.getProcessTemplateId();
        ProcessTemplate processTemplate = processTemplateService.getById(processTemplateId);

        // create a process
        Process process = new Process();
        BeanUtils.copyProperties(processFormVo, process);
        process.setStatus(1);
        String workNo = System.currentTimeMillis() + "";
        process.setProcessCode(workNo);
        process.setUserId(LoginUserInfoHelper.getUserId());
        process.setFormValues(processFormVo.getFormValues());
        process.setTitle(sysUser.getName() + "发起" + processTemplate.getName() + "申请");
        process.setStatus(1);
        processMapper.insert(process);

        // get processDefinitionKey
        String processDefinitionKey = processTemplate.getProcessDefinitionKey();
        Long businessKey = process.getId();

        // convert processFormVo json to map
        JSONObject jsonObject = JSON.parseObject(processFormVo.getFormValues());
        JSONObject formData = jsonObject.getJSONObject("formData");
        Map<String, Object> data = new HashMap<>();
        // get each key and value from formData
        for (String key : formData.keySet()) {
            String value = formData.getString(key);
            data.put(key, value);
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("data", data);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey.toString(), variables);
        // log to console of processInstance info
        log.info("processInstance id: {}, processInstance name: {}", processInstance.getId(), processInstance.getName());

        List<Task> currentTaskList = this.getCurrentTaskList(processInstance.getId());
        List<String> nameList = new ArrayList<>();
        for (Task task : currentTaskList) {
            SysUser nextAssigner = sysUserService.getUserByUserName(task.getAssignee());
            String name = nextAssigner.getName();
            nameList.add(name);

//             公众号消息推送
//            messageService.pushPendingMessage(process.getId(), nextAssigner.getId(), task.getId());
        }

        process.setProcessInstanceId(processInstance.getId());
        process.setDescription("waiting for " + StringUtils.join(nameList.toArray(), ",") + " to approve");
        // start up a process
        this.updateById(process);

        processRecordService.record(process.getId(), 1, "发起申请");
        return Result.ok();
    }

    private List<Task> getCurrentTaskList(String id) {
        TaskQuery taskQuery = taskService.createTaskQuery().processInstanceId(id);
        List<Task> list = taskQuery.list();
        return list;
    }

    @Override
    public IPage<ProcessVo> findPending(Page<Process> pageParam) {
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(LoginUserInfoHelper.getUsername()).orderByTaskCreateTime()
                .desc();
        List<Task> tasks = taskQuery.listPage((int) ((pageParam.getCurrent() - 1) * pageParam.getSize()), (int) pageParam.getSize());

        List<ProcessVo> processVoList = new ArrayList<>();
        for (Task task : tasks) {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            if (processInstance == null) {
                continue;
            }
            String businessKey = processInstance.getBusinessKey();
            Process process = this.getById(Long.parseLong(businessKey));
            if (process == null) {
                continue;
            }

            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVo.setTaskId(task.getId());
            processVoList.add(processVo);
        }


        IPage<ProcessVo> processVoIPage = new Page<>(pageParam.getCurrent(), pageParam.getSize(), processVoList.size());
        processVoIPage.setRecords(processVoList);
        return processVoIPage;
    }

    @Override
    public Map<String, Object> show(Long processId) {
        // get process by processId
        Process process = this.getById(processId);
        // get processTemplate by id
        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());
        // get processRecord by processId
        LambdaQueryWrapper<ProcessRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessRecord::getProcessId, processId);
        List<ProcessRecord> processRecords = processRecordService.list(wrapper);

        // check whether current user can approve
        boolean canApprove = false;
        List<Task> currentTaskList = this.getCurrentTaskList(process.getProcessInstanceId());
        for (Task task : currentTaskList) {
            if (task.getAssignee().equals(LoginUserInfoHelper.getUsername())) {
                canApprove = true;
                break;
            }
        }

        // return map
        Map<String, Object> map = new HashMap<>();
        map.put("process", process);
        map.put("processTemplate", processTemplate);
        map.put("processRecordList", processRecords);
        map.put("isApprove", canApprove ? 1 : 0);

        return map;
    }


    @Override
    public void approve(ApprovalVo approvalVo) {
        // get taskId
        String taskId = approvalVo.getTaskId();

        // get all process variables
        Map<String, Object> variables = taskService.getVariables(taskId);
        for (String key : variables.keySet()) {
            log.info("key: {}, value: {}", key, variables.get(key));
        }

        // 检查状态，1表示完成，-1表示驳回
        Integer status = approvalVo.getStatus();
        if (status == 1) {
            taskService.complete(taskId);
        } else {
            this.endTask(taskId);
        }

        // 记录审批相关信息
        String description = approvalVo.getStatus() == 1 ? "通过" : "驳回";
        processRecordService.record(approvalVo.getProcessId(), status, description);


        // 查询下一个审批人，更新流程表记录，process表
        Process process = this.getById(approvalVo.getProcessId());
        List<Task> currentTaskList = this.getCurrentTaskList(process.getProcessInstanceId());
        if (currentTaskList.size() == 0) {
            // 审批结束
            if (approvalVo.getStatus() == 1) {
                process.setDescription("审批通过");
                process.setStatus(2);

                // 公众号消息推送
//                messageService.pushProcessedMessage(process.getId(), process.getUserId(), 1);
            } else {
                process.setDescription("审批驳回");
                process.setStatus(-1);
                // 公众号消息推送
//                messageService.pushProcessedMessage(process.getId(), process.getUserId(), -1);
            }
            this.updateById(process);
        } else {
            // 获取当前任务的所有后面的审批人
            List<String> nameList = new ArrayList<>();
            for (Task task : currentTaskList) {
                SysUser nextAssigner = sysUserService.getUserByUserName(task.getAssignee());
                String name = nextAssigner.getName();
                nameList.add(name);

                // 公众号消息推送
//                messageService.pushPendingMessage(process.getId(), nextAssigner.getId(), task.getId());
            }
            process.setDescription("waiting for " + StringUtils.join(nameList.toArray(), ",") + " to approve");
            process.setStatus(1);
            this.updateById(process);

        }
    }

    private void endTask(String taskId) {
        // 获取任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 获取流程定义
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        List<EndEvent> endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        if (endEventList.size() == 0) {
            return ;
        }
        // 获取结束节点
        FlowNode endEvent = (FlowNode) endEventList.get(0);
        // 获取当前节点
        FlowNode curEvent = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        // 删除当前节点的所有流向
        List<FlowElement> flowElements = new ArrayList<>(curEvent.getOutgoingFlows());
        curEvent.getOutgoingFlows().clear();

        // 添加新的流向
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId("newSequenceFlow");
        sequenceFlow.setSourceFlowElement(curEvent);
        sequenceFlow.setTargetFlowElement(endEvent);


        // 当前节点指向新方向
        List<SequenceFlow> sequenceFlowList = new ArrayList<>();
        sequenceFlowList.add(sequenceFlow);
        curEvent.setOutgoingFlows(sequenceFlowList);

        taskService.complete(taskId);
    }

    @Override
    public IPage<ProcessVo> findProcessed(Page<Process> pageParam) {
        IPage<ProcessVo> pages = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        // 获取用户名
        String username = LoginUserInfoHelper.getUsername();

        // 查询已经处理的任务
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().taskAssignee(username)
                .orderByTaskCreateTime().asc()
                .listPage((int) ((pageParam.getCurrent() - 1) * pageParam.getSize()), (int) pageParam.getSize())
                ;

        // 封装到ProcessVo
        List<ProcessVo> processVoList = new ArrayList<>();
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            String processInstanceId = historicTaskInstance.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if (processInstance == null) {
                continue;
            }
            String businessKey = processInstance.getBusinessKey();
            Process process = this.getById(Long.parseLong(businessKey));
            if (process == null) {
                continue;
            }
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVo.setTaskId(historicTaskInstance.getId());
            processVoList.add(processVo);
        }
        pages.setRecords(processVoList);
        return pages;
    }

    @Override
    public IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam) {

        ProcessQueryVo processQueryVo = new ProcessQueryVo();
        processQueryVo.setUserId(LoginUserInfoHelper.getUserId());
        IPage<ProcessVo> startedProcessVoPages = baseMapper.selectPageVo(pageParam, processQueryVo);
        return startedProcessVoPages;
    }
}




