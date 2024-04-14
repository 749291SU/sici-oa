package org.sici.controller.wechat.config;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import util.JwtHelper;

/**
 * @projectName: oa-parent
 * @package: org.sici.controller.wechat.config
 * @className: WechatMpConfig
 * @author: 749291
 * @description: TODO
 * @date: 4/12/2024 22:10
 * @version: 1.0
 */

@Component
public class WechatMpConfig {
    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    @Bean
    public WxMpService wxMpService(WxMpConfigStorage wxMpConfigStorage){
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage(){
        WxMpDefaultConfigImpl wxMpConfigStorage = new WxMpDefaultConfigImpl();
        wxMpConfigStorage.setAppId(wechatAccountConfig.getMpAppId());
        wxMpConfigStorage.setSecret(wechatAccountConfig.getMpAppSecret());
//        wxMpConfigStorage.setTo
        return wxMpConfigStorage;
    }
}
