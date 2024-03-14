package org.sici.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sici.model.system.SysRole;
import org.sici.vo.system.AssginRoleVo;

import java.util.List;
import java.util.Map;

/**
* @author 20148
* @description 针对表【sys_role(角色)】的数据库操作Service
* @createDate 2024-01-30 19:45:08
*/
public interface SysRoleService extends IService<SysRole> {

    Map<String, List<SysRole>> findRoleByUserId(Long id);

    void doAssign(AssginRoleVo assignRoleVo);
}
