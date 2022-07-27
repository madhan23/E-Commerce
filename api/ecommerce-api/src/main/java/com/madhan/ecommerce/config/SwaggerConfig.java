package com.madhan.ecommerce.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(info = @Info(title = "E-Commerce API Services",version = "v1"))
@SecurityScheme(name = "JwtToken",type = SecuritySchemeType.HTTP,bearerFormat = "JWT",scheme ="Bearer")
public class SwaggerConfig {

}
