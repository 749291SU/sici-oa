package org.sici.util;

import org.sici.model.system.SysMenu;

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
}
