package org.sici.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.sici.model.system.SysUser;
import org.sici.result.Result;
import util.MD5;
import org.sici.service.SysUserService;
import org.sici.vo.system.SysUserQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @projectName: oa-parent
 * @package: org.sici.controller
 * @className: SysUserController
 * @author: 749291
 * @description: TODO
 * @date: 2/23/2024 5:00 PM
 * @version: 1.0
 */

@RestController
@RequestMapping("admin/system/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;
    // addUser
    @PostMapping("save")
    @Operation(summary = "添加用户")
    public Result save(@Parameter(name = "sysUser") @RequestBody SysUser sysUser) {
        sysUser.setPassword(MD5.encrypt(sysUser.getPassword()));
        sysUserService.save(sysUser);
        return Result.ok();
    }

    // removeUserById
    @DeleteMapping("remove/{id}")
    @Operation(summary = "删除用户")
    public Result remove(@Parameter(name = "id") @PathVariable("id") Long id) {
        sysUserService.removeById(id);
        return Result.ok();
    }

    // updateUser by Id
    @PutMapping("update")
    @Operation(summary = "更新用户")
    public Result update(@Parameter(name = "sysUser") @RequestBody SysUser sysUser) {
        if (!StringUtils.isEmpty(sysUser.getPassword())) {
            sysUser.setPassword(MD5.encrypt(sysUser.getPassword()));
        }
        sysUserService.updateById(sysUser);
        return Result.ok();
    }

    // findUser by Id
    @GetMapping("get/{id}")
    @Operation(summary = "查找用户")
    public Result find(@Parameter(name = "id") @PathVariable("id") Long id) {
        SysUser sysUser = sysUserService.getById(id);
        return Result.ok(sysUser);
    }


    // batchRemove
    @DeleteMapping("batchRemove")
    @Operation(summary = "批量删除用户")
    public Result batchRemove(@Parameter(name = "ids") @RequestBody List<Long> ids) {
        sysUserService.removeByIds(ids);
        return Result.ok();
    }

    // findAll
    @GetMapping("findAll")
    @Operation(summary = "查找所有用户")
    public Result findAll() {
        List<SysUser> list = sysUserService.list();
        return Result.ok(list);
    }

    // Page find
    @GetMapping("{page}/{limit}")
    @Operation(summary = "条件分页查询")
    public Result pageQuery(@Parameter(name = "page") @PathVariable("page") Integer page,
                            @Parameter(name = "limit") @PathVariable("limit") Integer limit,
                            @Parameter(name = "sysUserQueryVo") SysUserQueryVo sysUserQueryVo) {
        Page<SysUser> sysUserPage = new Page<>();
        sysUserPage.setSize(limit);
        sysUserPage.setCurrent(page);

        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        String username = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();

        if (username != null) {
            sysUserLambdaQueryWrapper.like(SysUser::getUsername, username);
        }
        if (createTimeBegin != null) {
            sysUserLambdaQueryWrapper.ge(SysUser::getCreateTime, createTimeBegin);
        }
        if (createTimeEnd != null) {
            sysUserLambdaQueryWrapper.le(SysUser::getCreateTime, createTimeEnd);
        }

        return Result.ok(sysUserService.page(sysUserPage, sysUserLambdaQueryWrapper));
    }


    // update user status by user id and status
    @GetMapping("updateStatus/{id}/{status}")
    @Operation(summary = "更新用户状态")
    public Result updateStatus(@Parameter(name = "id") @PathVariable("id") Long id,
                               @Parameter(name = "status") @PathVariable("status") Integer status) {
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        sysUser.setStatus(status);
        sysUserService.updateById(sysUser);
        return Result.ok();
    }
}
