package com.relatospapel.api_gateway.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

  @Value("${server.port:8080}")
  private String serverPort;

  @Bean
  public OpenAPI customOpenAPI() {
    Server gatewayServer = new Server();
    gatewayServer.setUrl("http://localhost:" + serverPort);
    gatewayServer.setDescription("API Gateway - Punto de entrada principal");

    Contact contact = new Contact();
    contact.setName("Relatos de Papel");
    contact.setEmail("contacto@relatospapel.com");

    Info info = new Info()
        .title("Relatos de Papel API")
        .version("1.0.0")
        .description("API Gateway centralizado para todos los microservicios de Relatos de Papel. " +
            "Incluye servicios de catálogo de libros y pagos.")
        .contact(contact);

    return new OpenAPI()
        .info(info)
        .servers(List.of(gatewayServer));
  }
}
