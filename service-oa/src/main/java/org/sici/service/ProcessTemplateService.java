package org.sici.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sici.model.process.ProcessTemplate;
import org.sici.result.Result;

/**
* @author 20148
* @description 针对表【oa_process_template(审批模板)】的数据库操作Service
* @createDate 2024-01-30 19:45:08
*/
public interface ProcessTemplateService extends IService<ProcessTemplate> {

    IPage<ProcessTemplate> selectPage(Integer page, Integer limit);

    Result publish(Long id);
}
