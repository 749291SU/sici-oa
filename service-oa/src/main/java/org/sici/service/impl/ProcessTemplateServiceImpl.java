package org.sici.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sici.model.process.ProcessTemplate;
import org.sici.service.ProcessTemplateService;
import org.sici.mapper.ProcessTemplateMapper;
import org.springframework.stereotype.Service;

/**
* @author 20148
* @description 针对表【oa_process_template(审批模板)】的数据库操作Service实现
* @createDate 2024-01-30 19:45:08
*/
@Service
public class ProcessTemplateServiceImpl extends ServiceImpl<ProcessTemplateMapper, ProcessTemplate>
    implements ProcessTemplateService {

}




