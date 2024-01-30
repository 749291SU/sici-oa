package org.sici.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sici.model.system.SysPost;
import org.sici.service.SysPostService;
import org.sici.mapper.SysPostMapper;
import org.springframework.stereotype.Service;

/**
* @author 20148
* @description 针对表【sys_post(岗位信息表)】的数据库操作Service实现
* @createDate 2024-01-30 19:45:08
*/
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost>
    implements SysPostService{

}




