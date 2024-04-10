package org.sici.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sici.model.system.SysUser;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.TypeVariable;
import java.util.Map;

/**
* @author 20148
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2024-01-30 19:45:08
*/
public interface SysUserService extends IService<SysUser> {

    SysUser getUserByUserName(String assignee);

    Map<String, Object> getCurrentUser();
}
