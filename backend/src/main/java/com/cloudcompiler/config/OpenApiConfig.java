package com.cloudcompiler.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cloudCompilerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cloud Compiler Development Environment API")
                        .description("REST API for Lexical Analysis, Syntax Analysis, AST Generation, Semantic Analysis, Symbol Table, and Intermediate Code Generation (TAC/Quadruples/Triples)")
                        .version("1.0.0")
                        .contact(new Contact().name("Cloud Compiler Team").email("developer@cloudcompiler.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .bearerFormat("JWT")
                                .scheme("bearer")));
    }
}
