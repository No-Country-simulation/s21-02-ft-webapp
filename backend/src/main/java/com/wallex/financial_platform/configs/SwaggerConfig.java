package com.wallex.financial_platform.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Documentation: Wallex - Sistema de Gestión Financiera")
                        .version("1.0")
                        .description("""
                                Documentación de la API de Wallex.
                                
                                📋 **Sistema de Gestión Financiera**
                                Plataforma segura para administrar dinero, transferencias, consulta de saldo,
                                gestión de tarjetas y notificaciones en tiempo real.
                                
                                🔗 **Repositorios:**
                                - Backend: [GitHub Backend](https://github.com/No-Country-simulation/s21-02-ft-webapp/tree/delmer-backup/backend)
                                - Frontend: [GitHub Frontend](https://github.com/No-Country-simulation/s21-02-ft-webapp/tree/delmer-backup/frontend)
                                
                                🚀 **Despliegue Frontend:** [Vercel](https://s21-02-ft-webapp-nlg4145xm-delmers-projects-b7fd56d9.vercel.app/)
                                """)
                        .contact(new Contact()
                                .name("Equipo Wallex")
                                .email("equipo@wallex.com")
                        )
                )
                // Servidores disponibles
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Servidor de Desarrollo Local"))
                .addServersItem(new Server()
                        .url("https://wallex-backend.onrender.com//swagger-ui/index.html")
                        .description("Servidor de Producción"))
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(new Components()
                        .addSecuritySchemes("JWT",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingresa tu token JWT. Obténlo desde /api/auth/login")
                        )
                );
    }
}
