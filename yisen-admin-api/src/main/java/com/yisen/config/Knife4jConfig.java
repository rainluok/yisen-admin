package com.yisen.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author rainluo
 * @version 1.0
 * @description Knife4j配置类，用于生成API文档
 * @date 2025/11/14 10:11
 */
@Configuration
public class Knife4jConfig {

    /**
     * API基本信息配置
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("益森管理系统API文档")
                        .version("1.0.0")
                        .description("益森管理系统后端API接口文档")
                        .contact(new Contact()
                                .name("益森开发团队")
                                .email("dev@yisen.com")
                                .url("https://www.yisen.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8082/api")
                                .description("开发环境"),
                        new Server()
                                .url("https://api.yisen.com")
                                .description("生产环境")
                ));
    }
}
