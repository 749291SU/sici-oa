package org.sici.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sici.mapper.SysUserMapper;
import org.sici.model.system.SysUser;
/**
 * @projectName: oa-parent
 * @package: org.sici.service.impl
 * @className: UserDetailService
 * @author: 749291
 * @description: TODO
 * @date: 3/9/2024 8:27 PM
 * @version: 1.0
 */

//@Service
public class UserDetailServiceImpl extends ServiceImpl<SysUserMapper, SysUser> {
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username);
//        SysUser one = getOne(wrapper);
//        if (one == null) {
//            throw new UsernameNotFoundException("用户不存在");
//        }
//        return new CustomUser(one, Collections.emptyList());
//    }
}
