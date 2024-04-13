package org.sici.controller.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @projectName: oa-parent
 * @package: org.sici.controller.wechat.config
 * @className: WechatAccountConfig
 * @author: 749291
 * @description: TODO
 * @date: 4/12/2024 22:09
 * @version: 1.0
 */

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {
    private String mpAppId;
    private String mpAppSecret;
}
