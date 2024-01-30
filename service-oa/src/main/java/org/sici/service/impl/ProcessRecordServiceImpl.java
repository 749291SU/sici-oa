package org.sici.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sici.model.process.ProcessRecord;
import org.sici.service.ProcessRecordService;
import org.sici.mapper.ProcessRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author 20148
* @description 针对表【oa_process_record(审批记录)】的数据库操作Service实现
* @createDate 2024-01-30 19:45:08
*/
@Service
public class ProcessRecordServiceImpl extends ServiceImpl<ProcessRecordMapper, ProcessRecord>
    implements ProcessRecordService {

}




