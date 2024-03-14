package org.sici.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sici.model.system.SysRole;
import org.sici.result.Result;
import org.sici.service.SysRoleService;
import org.sici.vo.system.AssginRoleVo;
import org.sici.vo.system.SysRoleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @projectName: oa-parent
 * @package: org.sici.controller
 * @className: SysRoleController
 * @author: 749291
 * @description: TODO
 * @date: 1/31/2024 12:15 PM
 * @version: 1.0
 */

@Tag(name = "角色", description = "角色管理")
@RestController
@RequestMapping("admin/system/sysRole")
public class SysRoleController {
    @Autowired
    SysRoleService sysRoleService;

    @Operation(summary = "查找所有角色", description = "查找所有角色")
    @GetMapping("findAll")
    public Result<List<SysRole>> findAll() {
        return Result.ok(sysRoleService.list());
    }

    @Operation(summary = "条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@Parameter(name = "page") @PathVariable("page") Integer page,
                                @Parameter(name = "limit") @PathVariable("limit") Integer limit,
                                @Parameter(name = "sysRoleQueryVo") SysRoleQueryVo sysRoleQueryVo) {
        Page<SysRole> sysRolePage = new Page<>();
        sysRolePage.setSize(limit);
        sysRolePage.setCurrent(page);
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if (roleName != null) {
            wrapper.like(SysRole::getRoleName, roleName);
        }
        Page<SysRole> pages = sysRoleService.page(sysRolePage, wrapper);
        return Result.ok(pages);
    }
    // remove SysRole by id
    @Operation(summary = "根据id删除角色")
    @DeleteMapping("remove/{id}")
    public Result removeById(@Parameter(name = "id") @PathVariable("id") Long id) {
        boolean b = sysRoleService.removeById(id);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // remove SysRole by ids
    @Operation(summary = "根据id批量删除角色")
    @DeleteMapping("batchRemove")
    public Result batchRemoveByIds(@Parameter(name = "ids") @RequestBody List<Long> ids) {
        boolean b = sysRoleService.removeByIds(ids);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // add SysRole
    @Operation(summary = "添加角色")
    @PostMapping("save")
    public Result save(@Parameter(name = "sysRole") @RequestBody SysRole sysRole) {
        boolean b = sysRoleService.save(sysRole);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // update SysRole
    @Operation(summary = "更新角色")
    @PutMapping("update")
    public Result update(@Parameter(name = "sysRole") @RequestBody SysRole sysRole) {
        boolean b = sysRoleService.updateById(sysRole);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // get SysRole by id
    @Operation(summary = "根据id查询角色")
    @GetMapping("get/{id}")
    public Result getById(@Parameter(name = "id") @PathVariable("id") Long id) {
        SysRole sysRole = sysRoleService.getById(id);
        if (sysRole == null) {
            return Result.fail();
        }
        return Result.ok(sysRole);
    }

    // get role list by user id
    @Operation(summary = "根据用户id查询角色")
    @GetMapping("toAssign/{id}")
    public Result toAssign(@Parameter(name = "id") @PathVariable("id") Long id) {
        Map<String, List<SysRole>> roleByUserId = sysRoleService.findRoleByUserId(id);
        return Result.ok(roleByUserId);
    }


    // assign role to user by assignRoleVo
    @Operation(summary = "给用户分配角色")
    @PostMapping("doAssign")
    public Result doAssign(@Parameter(name = "assignRoleVo") @RequestBody AssginRoleVo assignRoleVo) {
        sysRoleService.doAssign(assignRoleVo);
        return Result.ok();
    }
}
