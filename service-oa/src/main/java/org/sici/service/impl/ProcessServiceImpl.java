package org.sici.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.sici.model.process.Process;
import org.sici.service.ProcessService;
import org.sici.mapper.ProcessMapper;
import org.sici.vo.process.ProcessQueryVo;
import org.sici.vo.process.ProcessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
* @author 20148
* @description 针对表【oa_process(审批类型)】的数据库操作Service实现
* @createDate 2024-01-30 19:45:08
*/
@Service
@Slf4j
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process>
    implements ProcessService {
    @Autowired
    private ProcessMapper processMapper;
    @Autowired
    private RepositoryService repositoryService;

    @Override
    public IPage<ProcessVo> selectPage(IPage<ProcessVo> pagePram, ProcessQueryVo processQueryVo) {
        IPage<ProcessVo> processVoIPage = processMapper.selectPageVo(pagePram, processQueryVo);
        return processVoIPage;
    }

    @Override
    public void deployProcessByZip(String zipFilePath) {
        // 通过压缩包文件部署流程定义
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(zipFilePath);

        ZipInputStream zipInputStream = new ZipInputStream(resourceAsStream);

        Deployment deploy = repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
        log.info("---------deploy process success, deploy id: {}, deploy name:{}--------------", deploy.getId(), deploy.getId());
    }
}




