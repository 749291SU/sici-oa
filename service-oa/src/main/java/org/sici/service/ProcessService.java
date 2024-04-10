package org.sici.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.activiti.bpmn.model.Task;
import org.sici.model.process.Process;
import org.sici.model.process.ProcessRecord;
import org.sici.result.Result;
import org.sici.vo.process.ApprovalVo;
import org.sici.vo.process.ProcessFormVo;
import org.sici.vo.process.ProcessQueryVo;
import org.sici.vo.process.ProcessVo;

import java.util.List;
import java.util.Map;

/**
* @author 20148
* @description 针对表【oa_process(审批类型)】的数据库操作Service
* @createDate 2024-01-30 19:45:08
*/
public interface ProcessService extends IService<Process> {

    IPage<ProcessVo> selectPage(IPage<ProcessVo> pagePram, ProcessQueryVo processQueryVo);
    void deployProcessByZip(String zipFilePath);

    Result findProcessType();

    Result startUp(ProcessFormVo processFormVo);;

    IPage<ProcessVo> findPending(Page<Process> pageParam);

    Map<String, Object> show(Long processId);

    void approve(ApprovalVo approvalVo);

    IPage<ProcessVo> findProcessed(Page<Process> pageParam);

    IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam);
}
