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
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers("/api/employes/**").hasRole("ADMIN")
                        .requestMatchers("/api/organismes/**").hasAnyRole("ADMIN", "SECRETAIRE")
                        .requestMatchers("/api/projets/**").hasAnyRole("ADMIN", "SECRETAIRE", "CHEF_PROJET")
                        .requestMatchers("/api/phases/**").hasAnyRole("ADMIN", "CHEF_PROJET")
                        .requestMatchers("/api/factures/**").hasAnyRole("ADMIN", "COMPTABLE")
                        .requestMatchers("/api/reporting/**").hasAnyRole("ADMIN", "DIRECTEUR")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
}