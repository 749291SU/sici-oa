package org.sici.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sici.config.exception.SiwenException;
import org.sici.model.system.SysUser;
import org.sici.result.Result;
import util.JwtHelper;
import util.MD5;
import org.sici.service.SysMenuService;
import org.sici.service.SysUserService;
import org.sici.vo.system.LoginVo;
import org.sici.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @projectName: oa-parent
 * @package: org.sici.controller
 * @className: IndexController
 * @author: 749291
 * @description: TODO
 * @date: 2/1/2024 7:25 PM
 * @version: 1.0
 */

@Tag(name = "首页", description = "首页")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;
    // login
    @Operation(summary = "登录", description = "登录")
    @PostMapping("/login")
    public Result login(@Parameter(name = "loginVo") @RequestBody LoginVo loginVo) {
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        password = MD5.encrypt(password);

        SysUser one = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        if (one == null) {
            throw new SiwenException(201, "用户不存在");
        }
        if (!one.getPassword().equals(password)) {
            throw new SiwenException(202, "密码错误");
        }
        if (one.getStatus() == 0) {
            throw new SiwenException(203, "账号已被禁用");
        }

        String token = JwtHelper.createToken(one.getId(), one.getUsername());

        HashMap<Object, Object> map = new HashMap<>();
        map.put("token", token);

        return Result.ok(map);
    }

    // get user info
    @Operation(summary = "获取用户信息", description = "获取用户信息")
    @GetMapping("/info")
    public Result info(@Parameter(name = "token") @RequestHeader("token") String token) {
        // get user id
        Long userId = JwtHelper.getUserId(token);
        // get user
        SysUser user = sysUserService.getById(userId);

        List<RouterVo> routers = sysMenuService.findUserMenuListById(userId);

        List<String> perms = sysMenuService.findUserPermsByUserId(userId);

        HashMap<Object, Object> map = new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", user.getName());
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("routers", routers);
        map.put("buttons", perms);
        return Result.ok(map);
    }

    // logout
    @Operation(summary = "登出", description = "登出")
    @PostMapping("/logout")
    public Result logout() {
        return Result.ok();
    }
}
