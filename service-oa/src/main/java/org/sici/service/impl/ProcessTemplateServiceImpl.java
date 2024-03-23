package org.sici.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sici.model.process.ProcessTemplate;
import org.sici.model.process.ProcessType;
import org.sici.result.Result;
import org.sici.service.ProcessTemplateService;
import org.sici.mapper.ProcessTemplateMapper;
import org.sici.service.ProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 20148
* @description 针对表【oa_process_template(审批模板)】的数据库操作Service实现
* @createDate 2024-01-30 19:45:08
*/
@Service
public class ProcessTemplateServiceImpl extends ServiceImpl<ProcessTemplateMapper, ProcessTemplate>
    implements ProcessTemplateService {
    @Autowired
    private ProcessTypeService processTypeService;
    @Override
    public IPage<ProcessTemplate> selectPage(Integer page, Integer limit) {
        IPage<ProcessTemplate> processTemplatePage = new Page<>();
        processTemplatePage.setSize(limit);
        processTemplatePage.setCurrent(page);


        LambdaQueryWrapper<ProcessTemplate> processTemplateLambdaQueryWrapper = new LambdaQueryWrapper<>();
        processTemplateLambdaQueryWrapper.orderByAsc(ProcessTemplate::getId);

        IPage<ProcessTemplate> processTemplateIPage = super.page(processTemplatePage, processTemplateLambdaQueryWrapper);
        List<ProcessTemplate> records = processTemplateIPage.getRecords();


        for (ProcessTemplate record : records) {
            Long processTypeId = record.getProcessTypeId();
            // get process type name
            LambdaQueryWrapper<ProcessType> processTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            processTypeLambdaQueryWrapper.eq(ProcessType::getId, processTypeId);
            record.setProcessTypeName(processTypeService.getOne(processTypeLambdaQueryWrapper).getName());
        }
        return processTemplateIPage;
    }
}




