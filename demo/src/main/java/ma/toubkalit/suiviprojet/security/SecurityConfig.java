package ma.toubkalit.suiviprojet.security;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ── Public endpoints ──────────────────────────────
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // ── Employés ──────────────────────────────────────
                        .requestMatchers("/api/employes/disponibles").hasAnyRole("ADMIN", "CHEF_PROJET")
                        .requestMatchers("/api/employes/**").hasRole("ADMIN")

                        // ── Organismes ────────────────────────────────────
                        .requestMatchers("/api/organismes/**").hasAnyRole("ADMIN", "SECRETAIRE")

                        // ── Projets ───────────────────────────────────────
                        .requestMatchers("/api/projets/**").hasAnyRole("ADMIN", "SECRETAIRE", "CHEF_PROJET")

                        // ── Phases ────────────────────────────────────────
                        .requestMatchers("/api/phases/**").hasAnyRole("ADMIN", "CHEF_PROJET", "COMPTABLE")

                        // ── Affectations (phases/{id}/employes) ───────────
                        // Couvert par /api/phases/** et /api/employes/**

                        // ── Livrables ─────────────────────────────────────
                        .requestMatchers("/api/livrables/**").hasAnyRole("ADMIN", "CHEF_PROJET")

                        // ── Documents ─────────────────────────────────────
                        .requestMatchers("/api/documents/**").hasAnyRole("ADMIN", "CHEF_PROJET", "SECRETAIRE")

                        // ── Factures ──────────────────────────────────────
                        .requestMatchers("/api/factures/**").hasAnyRole("ADMIN", "COMPTABLE")

                        // ── Reporting ─────────────────────────────────────
                        .requestMatchers("/api/reporting/**").hasAnyRole("ADMIN", "DIRECTEUR")

                        // ── Tout le reste nécessite une authentification ──
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOrigins(java.util.Arrays.asList("http://localhost:5173", "http://localhost:3000"));
        configuration.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(java.util.Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
}