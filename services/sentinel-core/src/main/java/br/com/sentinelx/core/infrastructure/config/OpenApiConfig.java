package br.com.sentinelx.core.infrastructure.config;

import br.com.sentinelx.core.infrastructure.security.SentinelSecurityProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sentinelCoreOpenApi(SentinelSecurityProperties securityProperties) {
        return new OpenAPI()
                .info(new Info()
                        .title("Sentinel-Core API")
                        .description("REST API for agent event ingestion and historical vehicle event queries.")
                        .version("v1"))
                .components(new Components()
                        .addSecuritySchemes("basicAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                                .description("Administrative endpoints protected with HTTP Basic authentication."))
                        .addSecuritySchemes("agentApiKey", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(securityProperties.getAgent().getApiKeyHeader())
                                .description("Agent ingestion endpoints protected with the configured API key header.")));
    }
}