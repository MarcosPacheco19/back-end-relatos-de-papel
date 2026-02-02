package com.relatospapel.api_gateway.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi catalogueApi() {
    return GroupedOpenApi.builder()
        .group("Catálogo de Libros")
        .pathsToMatch("/catalogue/**")
        .build();
  }

  @Bean
  public GroupedOpenApi paymentsApi() {
    return GroupedOpenApi.builder()
        .group("Pagos")
        .pathsToMatch("/payments/**")
        .build();
  }

  @Bean
  public GroupedOpenApi allApis() {
    return GroupedOpenApi.builder()
        .group("Todos los Servicios")
        .pathsToMatch("/**")
        .build();
  }
}
