package org.sici.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sici.custom.CustomUser;
import org.sici.mapper.SysUserMapper;
import org.sici.model.system.SysUser;
import org.sici.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @projectName: oa-parent
 * @package: org.sici.service.impl
 * @className: UserDetailService
 * @author: 749291
 * @description: TODO
 * @date: 3/9/2024 8:27 PM
 * @version: 1.0
 */

@Service
public class UserDetailsServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserDetailsService {
    @Autowired
    private SysMenuService sysMenuService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username);
        SysUser one = getOne(wrapper);
        if (one == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 根据用户名查询用户权限
        List<String> perms = sysMenuService.findUserPermsByUserId(one.getId());
        List<SimpleGrantedAuthority> authorities = perms.stream().flatMap(authority -> Collections.singletonList(new SimpleGrantedAuthority(authority.trim())).stream()).collect(Collectors.toList());

        return new CustomUser(one, authorities);
    }
}
