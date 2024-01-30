package org.sici.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sici.model.system.SysUser;
import org.sici.service.SysUserService;
import org.sici.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
* @author 20148
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2024-01-30 19:45:08
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

}




