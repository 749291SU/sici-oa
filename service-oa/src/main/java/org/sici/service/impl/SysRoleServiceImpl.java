package org.sici.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sici.mapper.SysUserRoleMapper;
import org.sici.model.system.SysRole;
import org.sici.model.system.SysUserRole;
import org.sici.service.SysRoleService;
import org.sici.mapper.SysRoleMapper;
import org.sici.service.SysUserRoleService;
import org.sici.service.SysUserService;
import org.sici.vo.system.AssginRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author 20148
* @description 针对表【sys_role(角色)】的数据库操作Service实现
* @createDate 2024-01-30 19:45:08
*/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements SysRoleService{
    @Autowired
    private SysRoleMapper sysRoleMapper;
//    @Autowired
//    private SysUserRoleService sysUserRole;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public Map<String, List<SysRole>> findRoleByUserId(Long id) {
        List<SysRole> allSysRoles = sysRoleMapper.selectList(null);

        LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserRoleLambdaQueryWrapper.eq(SysUserRole::getUserId, id);
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(sysUserRoleLambdaQueryWrapper);
        List<Long> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        ArrayList<SysRole> sysRoles = new ArrayList<>();
        for (SysRole sysRole : allSysRoles) {
            if (roleIds.contains(sysRole.getId())) {
                sysRoles.add(sysRole);
            }
        }

        HashMap<String, List<SysRole>> stringListHashMap = new HashMap<>();
        stringListHashMap.put("assginRoleList", sysRoles);
        stringListHashMap.put("allRolesList", allSysRoles);

        return stringListHashMap;
    }

    @Override
    public void doAssign(AssginRoleVo assignRoleVo) {
        LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserRoleLambdaQueryWrapper.eq(SysUserRole::getUserId, assignRoleVo.getUserId());
        sysUserRoleMapper.delete(sysUserRoleLambdaQueryWrapper);

        List<Long> roleIdList = assignRoleVo.getRoleIdList();
        for (Long roleId : roleIdList) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assignRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleMapper.insert(sysUserRole);
        }
    }
}




