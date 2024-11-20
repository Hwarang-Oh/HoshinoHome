package com.ssafy.hoshinohome.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info().title("SSAFY API 명세서");

        return new OpenAPI().components(new Components()).info(info);
    }

//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder().group("ssafy-admin").pathsToMatch("/admin/**").build();
//    }

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder().group("api").pathsToMatch("/api/**").build();
    }

//    @Bean
//    public GroupedOpenApi fileApi() {
//        return GroupedOpenApi.builder().group("ssafy-file").pathsToMatch("/file/**").build();
//    }

}
