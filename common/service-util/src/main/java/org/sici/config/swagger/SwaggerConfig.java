package org.sici.config.swagger;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @projectName: oa-parent
 * @package: org.sici.config
 * @className: SwaggerConfig
 * @author: 749291
 * @description: TODO
 * @date: 1/31/2024 12:54 PM
 * @version: 1.0
 */

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI docsOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("oa docs API")
                        .description("oa docs api")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringShop Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs"));
    }

    @Bean
    public GroupedOpenApi sysRoleApi() {
        return GroupedOpenApi.builder()
                .group("角色管理")
                .pathsToMatch("/admin/system/sysRole/**")
                .build();
    }

    @Bean
    public GroupedOpenApi sysUserApi() {
        return GroupedOpenApi.builder()
                .group("用户管理")
                .pathsToMatch("/admin/system/sysUser/**")
                .build();
    }

    @Bean
    public GroupedOpenApi sysMenuApi() {
        return GroupedOpenApi.builder()
                .group("菜单管理")
                .pathsToMatch("/admin/system/sysMenu/**")
                .build();
    }

    @Bean
    public GroupedOpenApi index() {
        return GroupedOpenApi.builder()
                .group("首页")
                .pathsToMatch("/admin/system/index/**")
                .build();
    }
}
