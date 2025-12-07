package com.cusco.limpio.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
	return new OpenAPI()
		.components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME,
			new SecurityScheme()
				.name(SECURITY_SCHEME_NAME)
				.type(SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT")
		))
		.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
		.info(new Info()
			.title("Cusco Limpio API")
			.version("v0.0.1")
			.description("API para gestionar reportes de limpieza en Cusco")
			.contact(new Contact().name("Equipo Cusco Limpio").email("soporte@cusco-limpio.example"))
			.license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
		);
    }
}
