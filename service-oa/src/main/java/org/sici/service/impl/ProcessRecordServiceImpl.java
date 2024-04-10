package org.sici.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sici.custom.LoginUserInfoHelper;
import org.sici.model.process.Process;
import org.sici.model.process.ProcessRecord;
import org.sici.model.system.SysUser;
import org.sici.service.ProcessRecordService;
import org.sici.mapper.ProcessRecordMapper;
import org.sici.service.ProcessService;
import org.sici.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 20148
* @description 针对表【oa_process_record(审批记录)】的数据库操作Service实现
* @createDate 2024-01-30 19:45:08
*/
@Service
public class ProcessRecordServiceImpl extends ServiceImpl<ProcessRecordMapper, ProcessRecord>
    implements ProcessRecordService {
    @Autowired
    private ProcessRecordMapper processRecordMapper;
    @Autowired
    private ProcessService processService;
    @Autowired
    private SysUserService sysUserService;
    @Override
    public void record(Long processId, Integer status, String description) {
        Long userId = LoginUserInfoHelper.getUserId();
        SysUser sysUser = sysUserService.getById(userId);
        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessId(processId);
        processRecord.setStatus(status);
        processRecord.setDescription(description);
        processRecord.setOperateUser(sysUser.getName());
        processRecord.setOperateUserId(userId);
        processRecordMapper.insert(processRecord);
    }
}




