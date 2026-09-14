package com.example.shopping.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization"))) // Swagger UI에서 자동 주입
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .addServersItem(new Server().url("/"))
                .info(apiInfo());
    }


    private Info apiInfo() {
        return new Info()
                .title("My Salon")
                .description("My Salon의 api입니다.")
                .version("1.0");
    }
    /*@Bean
    public OpenAPI customOpenAPI() {

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        return new OpenAPI()
                .info(new Info()
                        .title("MySalon API")
                        .description("MySalon 홈쇼핑 api")
                        .version("1.0.0"))
                .components(new Components()
                        // 위에서 정의한 SecurityScheme을 등록
                        .addSecuritySchemes("BearerAuth", securityScheme))
                // 모든 API 엔드포인트에 대해 이 SecurityRequirement를 적용
                .addSecurityItem(securityRequirement);
    }*/


}



