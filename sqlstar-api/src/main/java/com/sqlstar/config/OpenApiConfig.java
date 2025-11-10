package com.sqlstar.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SQL Star API",
                version = "1.0.0",
                description = "REST API for managing sensitive words and censoring messages."
        )
)
public class OpenApiConfig {
}
