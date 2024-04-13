package org.sici.util;

import org.sici.model.system.SysMenu;
import org.sici.vo.wechat.MenuVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @projectName: oa-parent
 * @package: org.sici.util
 * @className: MenuHelper
 * @author: 749291
 * @description: TODO
 * @date: 2/27/2024 8:34 PM
 * @version: 1.0
 */

public class MenuHelper {
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        ArrayList<SysMenu> trees = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            if (sysMenu.getParentId() == 0) {
                trees.add(sysMenu);
            }
        }

        for (SysMenu sysMenu : sysMenuList) {
            sysMenu.setChildren(new ArrayList<>());
        }

        for (SysMenu sysMenu : sysMenuList) {
            for (SysMenu menu : sysMenuList) {
                if (sysMenu.getId().equals(menu.getParentId())) {
                    sysMenu.getChildren().add(menu);
                }
            }
        }

        return trees;
    }

    public static List<MenuVo> buildTreeOfMenuVo(List<MenuVo> menuVoListenuList) {
        ArrayList<MenuVo> trees = new ArrayList<>();
        for (MenuVo menuVo : menuVoListenuList) {
            if (menuVo.getParentId() == 0) {
                trees.add(menuVo);
            }
        }

        for (MenuVo menuVo : menuVoListenuList) {
            menuVo.setChildren(new ArrayList<>());
        }

        for (MenuVo menuVo : menuVoListenuList) {
            for (MenuVo menuVo1 : menuVoListenuList) {
                if (menuVo.getId().equals(menuVo1.getParentId())) {
                    menuVo.getChildren().add(menuVo1);
                }
            }
        }

        return trees;
    }
}
