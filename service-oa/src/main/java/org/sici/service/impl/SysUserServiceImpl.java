package org.sici.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sici.custom.LoginUserInfoHelper;
import org.sici.model.system.SysUser;
import org.sici.service.SysUserService;
import org.sici.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author 20148
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2024-01-30 19:45:08
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{
    @Override
    public SysUser getUserByUserName(String userName) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, userName);
        return this.getOne(wrapper);
    }


    @Override
    public Map<String, Object> getCurrentUser() {
        Long userId = LoginUserInfoHelper.getUserId();

        SysUser currentUser = getById(userId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", currentUser.getName());
        map.put("phone", currentUser.getPhone());
        return map;
    }
}




