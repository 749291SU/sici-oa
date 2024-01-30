package org.sici.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sici.model.system.SysOperLog;
import org.sici.service.SysOperLogService;
import org.sici.mapper.SysOperLogMapper;
import org.springframework.stereotype.Service;

/**
* @author 20148
* @description 针对表【sys_oper_log(操作日志记录)】的数据库操作Service实现
* @createDate 2024-01-30 19:45:08
*/
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog>
    implements SysOperLogService{

}




