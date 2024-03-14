package org.sici.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.sici.model.system.SysMenu;
import org.sici.result.Result;
import org.sici.service.SysMenuService;
import org.sici.vo.system.AssginMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @projectName: oa-parent
 * @package: org.sici.controller
 * @className: SysMenuController
 * @author: 749291
 * @description: TODO
 * @date: 2/27/2024 8:17 PM
 * @version: 1.0
 */

@RestController
@RequestMapping("admin/system/sysMenu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;
    // save SysMenu
    @Operation(summary = "新增菜单")
    @PostMapping("save")
    public Result save(@Parameter(name = "sysMenu") @RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return Result.ok();
    }

    // remove SysMenu by id
    @Operation(summary = "根据id删除菜单")
    @DeleteMapping("remove/{id}")
    public Result removeById(@Parameter(name = "id") @PathVariable("id") Long id) {
        sysMenuService.removeMenuById(id);
        return Result.ok();
    }

    // remove SysMenu by ids
    @Operation(summary = "根据id批量删除菜单")
    @DeleteMapping("batchRemove")
    public Result batchRemoveByIds(@Parameter(name = "ids") @RequestBody List<Long> ids) {
        boolean b = sysMenuService.removeByIds(ids);
        return Result.ok();
    }


    // update SysMenu by id
    @Operation(summary = "根据id修改菜单")
    @PutMapping("update")
    public Result updateById(@Parameter(name = "sysMenu") @RequestBody SysMenu sysMenu) {
        boolean b = sysMenuService.updateById(sysMenu);
        return Result.ok();
    }

    // find all SysMenu
    @Operation(summary = "查找所有菜单")
    @GetMapping("findNodes")
    public Result<List<SysMenu>> findNodes() {
        return Result.ok(sysMenuService.findNodes());
    }


    // 根据角色id查询其菜单
    @Operation(summary = "根据角色id查询其菜单")
    @GetMapping("toAssign/{id}")
    public Result toAssign(@Parameter(name = "id") @PathVariable("id") Long id) {
        List<SysMenu> sysMenus = sysMenuService.findMenuByRoleId(id);
        return Result.ok(sysMenus);
    }


    // 通过assginMenuVo为角色分配菜单
    @Operation(summary = "为角色分配菜单")
    @PostMapping("doAssign")
    public Result doAssign(@Parameter(name = "assginMenuVo") @RequestBody AssginMenuVo assginMenuVo) {
        sysMenuService.doAssign(assginMenuVo);
        return Result.ok();
    }

}
