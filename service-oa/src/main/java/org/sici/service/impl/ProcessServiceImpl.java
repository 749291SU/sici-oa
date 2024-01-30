package org.sici.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sici.model.process.Process;
import org.sici.service.ProcessService;
import org.sici.mapper.ProcessMapper;
import org.springframework.stereotype.Service;

/**
* @author 20148
* @description 针对表【oa_process(审批类型)】的数据库操作Service实现
* @createDate 2024-01-30 19:45:08
*/
@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process>
    implements ProcessService {

}




