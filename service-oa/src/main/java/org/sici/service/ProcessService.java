package org.sici.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sici.model.process.Process;
import org.sici.vo.process.ProcessQueryVo;
import org.sici.vo.process.ProcessVo;

/**
* @author 20148
* @description 针对表【oa_process(审批类型)】的数据库操作Service
* @createDate 2024-01-30 19:45:08
*/
public interface ProcessService extends IService<Process> {

    IPage<ProcessVo> selectPage(IPage<ProcessVo> pagePram, ProcessQueryVo processQueryVo);
    void deployProcessByZip(String zipFilePath);
}
