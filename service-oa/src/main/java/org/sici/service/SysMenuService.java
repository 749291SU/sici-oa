package org.sici.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sici.model.system.SysMenu;
import org.sici.vo.system.AssginMenuVo;
import org.sici.vo.system.RouterVo;

import java.util.List;

/**
* @author 20148
* @description 针对表【sys_menu(菜单表)】的数据库操作Service
* @createDate 2024-01-30 19:45:08
*/
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();

    void removeMenuById(Long id);

    List<SysMenu> findMenuByRoleId(Long id);

    void doAssign(AssginMenuVo assginMenuVo);

    List<RouterVo> findUserMenuListById(Long userId);

    List<String> findUserPermsByUserId(Long userId);

    List<RouterVo> buildRouter(List<SysMenu> userMenusByIdTrees);
}
