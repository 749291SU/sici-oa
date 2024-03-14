package org.sici.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sici.config.exception.SiwenException;
import org.sici.mapper.SysRoleMenuMapper;
import org.sici.model.system.SysMenu;
import org.sici.model.system.SysRoleMenu;
import org.sici.service.SysMenuService;
import org.sici.mapper.SysMenuMapper;
import org.sici.util.MenuHelper;
import org.sici.vo.system.AssginMenuVo;
import org.sici.vo.system.MetaVo;
import org.sici.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 20148
* @description 针对表【sys_menu(菜单表)】的数据库操作Service实现
* @createDate 2024-01-30 19:45:08
*/
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements SysMenuService{
    @Autowired
    SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> findNodes() {
        List<SysMenu> sysMenuList = baseMapper.selectList(null);
        List<SysMenu> trees = MenuHelper.buildTree(sysMenuList);
        return trees;
    }

    @Override
    public void removeMenuById(Long id) {
        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysMenuLambdaQueryWrapper.eq(SysMenu::getParentId, id);
        Long count = baseMapper.selectCount(sysMenuLambdaQueryWrapper);
        if (count > 0) {
            throw new SiwenException(201, "菜单不能删除");
        } else {
            baseMapper.deleteById(id);
        }
//        List<SysMenu> sysMenus = baseMapper.selectList(null);
//        ArrayList<Long> toDelete = new ArrayList<>();
//        removeSubTree(id, sysMenus, toDelete);
//        baseMapper.deleteBatchIds(toDelete);
    }

//    private void removeSubTree(Long id, List<SysMenu> sysMenus, List<Long> toDelete) {
//        for (SysMenu sysMenu : sysMenus) {
//            if (sysMenu.getParentId().equals(id)) {
//                removeSubTree(sysMenu.getId(), sysMenus, toDelete);
//            }
//        }
//        toDelete.add(id);
//    }


    @Override
    public List<SysMenu> findMenuByRoleId(Long id) {
        // 查询所有status为1的所有菜单
        List<SysMenu> allSysMenus = baseMapper.selectList(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1));

        // 根据角色id查询所有菜单
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));
        //根据菜单id查询所有菜单
        List<Long> menuIds = new ArrayList<>();
        for (SysRoleMenu sysRoleMenu : sysRoleMenus) {
            menuIds.add(sysRoleMenu.getMenuId());
        }

        // 将被选中的菜单的select设为true
        for (SysMenu sysMenu : allSysMenus) {
            if (menuIds.contains(sysMenu.getId())) {
                sysMenu.setSelect(true);
            }
        }

        // 构建树形结构
        List<SysMenu> trees = MenuHelper.buildTree(allSysMenus);
        return trees;
    }

    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        Long roleId = assginMenuVo.getRoleId();
        List<Long> menuIdList = assginMenuVo.getMenuIdList();

        // 删除之前分配的菜单，重新分配
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        if (menuIdList.size() > 0) {
            List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
            for (Long menuId : menuIdList) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenu.setMenuId(menuId);
                sysRoleMenus.add(sysRoleMenu);
            }
            for (SysRoleMenu sysRoleMenu : sysRoleMenus) {
                sysRoleMenuMapper.insert(sysRoleMenu);
            }
        }

    }

    @Override
    public List<RouterVo> findUserMenuListById(Long userId) {
        List<SysMenu> userMenuListById = null;
        // 是管理员
        if (userId == 1) {
            userMenuListById = baseMapper.selectList(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1).orderByAsc(SysMenu::getSortValue));
        } else {
            userMenuListById = baseMapper.findUserMenuListByUserId(userId);
        }
        List<SysMenu> userMenusByIdTrees = MenuHelper.buildTree(userMenuListById);
        List<RouterVo> routerVos = this.buildRouter(userMenusByIdTrees);
        return routerVos;
    }

    @Override
    public List<String> findUserPermsByUserId(Long userId) {
        List<SysMenu> sysMenuList = null;
        // 如果是管理员，返回所有
        if (userId == 1) {
            sysMenuList = baseMapper.selectList(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getType, 2).eq(SysMenu::getStatus, 1).orderByAsc(SysMenu::getSortValue));
        } else {
            sysMenuList = baseMapper.findUserMenuListByUserId(userId);
        }

        List<String> buttons = sysMenuList.stream().filter(item -> item.getType() == 2).map(SysMenu::getPerms).collect(Collectors.toList());
        return buttons;
    }

    @Override
    public List<RouterVo> buildRouter(List<SysMenu> userMenusByIdTrees) {
        List<RouterVo> routers = new ArrayList<>();
        for (SysMenu sysMenu : userMenusByIdTrees) {
            RouterVo routerVo = new RouterVo();
            routerVo.setPath(getRouterPath(sysMenu));
            routerVo.setComponent(sysMenu.getComponent());
            routerVo.setMeta(new MetaVo(sysMenu.getName(), sysMenu.getIcon()));
            routerVo.setHidden(false);
            routerVo.setAlwaysShow(false);

            List<SysMenu> children = sysMenu.getChildren();

            if (sysMenu.getType().intValue() == 1) {
                List<SysMenu> hidden = children.stream().filter(item -> !StringUtils.isEmpty(item.getComponent()))
                        .collect(Collectors.toList());
                for (SysMenu sysMenu1 : hidden) {
                    RouterVo routerVo1 = new RouterVo();
                    routerVo1.setPath(getRouterPath(sysMenu1));
                    routerVo1.setComponent(sysMenu1.getComponent());
                    routerVo1.setMeta(new MetaVo(sysMenu1.getName(), sysMenu1.getIcon()));
                    routerVo1.setHidden(true);
                    routerVo1.setAlwaysShow(false);
                    routers.add(routerVo1);
                }
            } else {
                if (!CollectionUtils.isEmpty(children)) {
                    if (children.size() > 0) {
                        routerVo.setAlwaysShow(true);
                    }
                    routerVo.setChildren(buildRouter(children));
                }
            }
            routers.add(routerVo);
        }
        return routers;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(@NotNull SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if(menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }
}




