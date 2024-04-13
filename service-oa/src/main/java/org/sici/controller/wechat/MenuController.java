package org.sici.controller.wechat;

import io.swagger.annotations.ApiOperation;
import org.sici.result.Result;
import org.sici.service.WechatMenuService;
import org.sici.vo.wechat.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @projectName: oa-parent
 * @package: org.sici.controller.wechat
 * @className: MenuController
 * @author: 749291
 * @description: TODO
 * @date: 4/12/2024 21:33
 * @version: 1.0
 */

@RestController
@RequestMapping("/admin/wechat/menu")
public class MenuController {
    @Autowired
    private WechatMenuService wechatMenuService;
    // 获取全部菜单
    @ApiOperation(value = "find menu info")
    @GetMapping("findMenuInfo")
    public Result findMenuInfo() {
        List<MenuVo> menuInfo = wechatMenuService.findMenuInfo();
        return Result.ok(menuInfo);
    }

    // 同步菜单
    //@PreAuthorize("hasAuthority('bnt.menu.syncMenu')")
    @ApiOperation(value = "同步菜单")
    @GetMapping("syncMenu")
    public Result createMenu() {
        wechatMenuService.syncMenu();
        return Result.ok();
    }


    // 删除同步菜单
    //@PreAuthorize("hasAuthority('bnt.menu.syncMenu')")
    @ApiOperation(value = "删除同步菜单")
    @GetMapping("removeMenu")
    public Result deleteMenu() {
        wechatMenuService.removeMenu();
        return Result.ok();
    }

}
