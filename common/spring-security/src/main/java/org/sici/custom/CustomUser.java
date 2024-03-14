package org.sici.custom;

import org.sici.model.system.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @projectName: oa-parent
 * @package: org.sici.custom
 * @className: CustomUser
 * @author: 749291
 * @description: TODO
 * @date: 3/9/2024 4:40 PM
 * @version: 1.0
 */

public class CustomUser extends User {
    private SysUser sysUser;

    public CustomUser(SysUser sysUser, Collection<? extends GrantedAuthority> authorities) {
        super(sysUser.getUsername(), sysUser.getPassword(), authorities);
        this.sysUser = sysUser;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }
}
