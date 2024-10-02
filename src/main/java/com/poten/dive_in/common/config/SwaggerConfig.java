package com.poten.dive_in.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${api-url}")
    private String apiUrl;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                // 로컬 및 배포 서버 URL 설정
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Server"),  // 로컬 서버
                        new Server().url(apiUrl).description("Production Server")  // 배포 서버
                ));
    }

    private Info apiInfo() {
        return new Info()
                .title("dive-in-API")
                .version("1.0.0");
    }
}
