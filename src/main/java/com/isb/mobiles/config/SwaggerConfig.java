package com.isb.mobiles.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Mobile Subscriptions API",
                "With Mobile Subscriptions REST API you can manage mobile subscriptions. It provides support for creation and deletion of mobile subscriptions, updating and retrieving the records. ",
                "API 1.0",
                "",
                new Contact("Joana Goshevska", "https://github.com/JussV", "joana.veljanoska@gmail.com"),
                "", "", Collections.emptyList());
    }
}
