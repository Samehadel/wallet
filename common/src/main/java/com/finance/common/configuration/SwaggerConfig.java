package com.finance.common.configuration;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;

import java.util.Collections;

@Configuration
public class SwaggerConfig {


    @Value("${swagger.info.title:}")
    private String title;

    @Value("${swagger.info.version:}")
    private String version;

    @Value("${swagger.info.description:}")
    private String description;

    @Value("${swagger.info.terms-of-Service:}")
    private String termsOFService;

    @Value("${swagger.info.license.name:}")
    private String licenseName;

    @Value("${swagger.info.license.url:}")
    private String licenseUrl;


    @Bean
    public OpenAPI swaggerDocumentInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description(description)
                        .termsOfService(termsOFService)
                        .license(new License().name(licenseName).url(licenseUrl)))
                .tags(Collections.emptyList())
                .servers(null);
    }
}
