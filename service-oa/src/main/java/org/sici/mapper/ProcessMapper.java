package org.sici.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.sici.model.process.Process;
import org.sici.vo.process.ProcessQueryVo;
import org.sici.vo.process.ProcessVo;

/**
* @author 20148
* @description 针对表【oa_process(审批类型)】的数据库操作Mapper
* @createDate 2024-01-30 19:45:08
* @Entity org.sici.pojo.OaProcess
*/
public interface ProcessMapper extends BaseMapper<Process> {
    // selectPageVo
    IPage<ProcessVo> selectPageVo(IPage<ProcessVo> pagePram, @Param("vo") ProcessQueryVo processQueryVo);
}




