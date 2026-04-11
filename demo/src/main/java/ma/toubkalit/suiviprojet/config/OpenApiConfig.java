package ma.toubkalit.suiviprojet.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.*;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Suivi de Projet")
                        .version("1.0.0")
                        .description("""
                                Documentation complète de l'API de gestion des projets.
                                
                                ## Authentification
                                Toutes les routes (sauf `/api/auth/login`) nécessitent un token **JWT Bearer**.
                                
                                ## Rôles disponibles
                                - `ADMIN` — Accès total
                                - `CHEF_PROJET` — Gestion phases, affectations, livrables
                                - `SECRETAIRE` — Gestion documents, organismes
                                - `COMPTABLE` — Accès factures
                                - `DIRECTEUR` — Lecture reporting
                                """)
                        .contact(new Contact()
                                .name("Équipe SuiviProjet")
                                .email("admin@suiviprojet.ma"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT"))
                )
                .servers(List.of(
                        new Server().url("http://localhost:8082").description("Serveur de développement"),
                        new Server().url("http://backend:8082").description("Serveur Docker")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Entrez votre token JWT ici (sans le préfixe 'Bearer ')")
                        )
                );
    }
}