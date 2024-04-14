package org.sici.controller.wechat;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.jsonwebtoken.Jwt;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.sici.model.system.SysUser;
import org.sici.result.Result;
import org.sici.service.SysUserService;
import org.sici.vo.wechat.BindPhoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import util.JwtHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @projectName: oa-parent
 * @package: org.sici.controller.wechat
 * @className: WeChatController
 * @author: 749291
 * @description: TODO
 * @date: 4/13/2024 16:45
 * @version: 1.0
 */

@Controller
@RequestMapping("/admin/wechat")
@Slf4j
@CrossOrigin
public class WeChatController {

    @Resource
    private SysUserService sysUserService;

    @Autowired
    private WxMpService wxMpService;

    @Value("${wechat.userInfoUrl}")
    private String userInfoUrl;

    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) throws UnsupportedEncodingException {
        String redirectedUrl = wxMpService.getOAuth2Service().buildAuthorizationUrl(userInfoUrl, WxConsts.OAuth2Scope.SNSAPI_USERINFO,
                URLEncoder.encode(returnUrl.replace("sicioa", "#"), "UTF-8"));
        return "redirect:" + redirectedUrl;
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) throws WxErrorException {
        // 获取accessToken
        WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);

        // 获取openid
        String openId = accessToken.getOpenId();
        log.info("openid={}", openId);

        // 获取用户信息
        WxOAuth2UserInfo wxMpUserInfo = wxMpService.getOAuth2Service().getUserInfo(accessToken, null);
        log.info("userInfo={}", wxMpUserInfo);

        // 根据openid查询用户信息
        SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getOpenId, openId));

        String token = "";
        if (sysUser != null) {
            // 已经绑定过
            token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        }
        if(returnUrl.indexOf("?") == -1) {
            return "redirect:" + returnUrl + "?token=" + token + "&openId=" + openId;
        } else {
            return "redirect:" + returnUrl + "&token=" + token + "&openId=" + openId;
        }
    }


    @ApiOperation(value = "绑定手机号")
    @PostMapping("/bindPhone")
    @ResponseBody
    public Result bindPhone(@RequestBody BindPhoneVo bindPhoneVo) {
        String phone = bindPhoneVo.getPhone();
        String openId = bindPhoneVo.getOpenId();
        SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhone, phone));
        if (sysUser == null) {
            return Result.fail("手机号不存在");
        } else {
            sysUser.setOpenId(openId);
            sysUserService.updateById(sysUser);
            String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
            return Result.ok(token);
        }
    }
}

