package org.sici.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sici.model.system.SysLoginLog;
import org.sici.service.SysLoginLogService;
import org.sici.mapper.SysLoginLogMapper;
import org.springframework.stereotype.Service;

/**
* @author 20148
* @description 针对表【sys_login_log(系统访问记录)】的数据库操作Service实现
* @createDate 2024-01-30 19:45:08
*/
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog>
    implements SysLoginLogService{

}




