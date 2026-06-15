package com.techchallenger.oficina360.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Clientes
                        .requestMatchers(HttpMethod.GET, "/clientes/**")
                        .hasAnyRole("ADMIN", "ATENDENTE")

                        .requestMatchers(HttpMethod.POST, "/clientes/**")
                        .hasAnyRole("ADMIN", "ATENDENTE")

                        .requestMatchers(HttpMethod.PUT, "/clientes/**")
                        .hasAnyRole("ADMIN", "ATENDENTE")

                        .requestMatchers(HttpMethod.DELETE, "/clientes/**")
                        .hasRole("ADMIN")

                        // Veículos
                        .requestMatchers("/veiculos/**")
                        .hasAnyRole("ADMIN", "ATENDENTE")

                        // Serviços
                        .requestMatchers("/servicos/**")
                        .hasRole("ADMIN")

                        // Estoque
                        .requestMatchers("/estoque/**")
                        .hasAnyRole("ADMIN", "ESTOQUISTA")

                        // Diagnóstico
                        .requestMatchers(HttpMethod.PATCH, "/ordens-servico/*/diagnostico")
                        .hasAnyRole("ADMIN", "MECANICO")

                        // Aprovação do orçamento
                        .requestMatchers(HttpMethod.PATCH, "/ordens-servico/*/aprovacao")
                        .hasAnyRole("ADMIN", "CLIENTE")

                        // Execução
                        .requestMatchers(HttpMethod.PATCH, "/ordens-servico/*/execucao/**")
                        .hasAnyRole("ADMIN", "MECANICO")

                        // Entrega
                        .requestMatchers(HttpMethod.PATCH, "/ordens-servico/*/entrega")
                        .hasAnyRole("ADMIN", "ATENDENTE")

                        // Ordem de serviço geral
                        .requestMatchers(HttpMethod.POST, "/ordens-servico/**")
                        .hasAnyRole("ADMIN", "ATENDENTE")

                        .requestMatchers(HttpMethod.GET, "/ordens-servico/**")
                        .hasAnyRole("ADMIN", "ATENDENTE", "MECANICO")

                        .requestMatchers(HttpMethod.PUT, "/ordens-servico/**")
                        .hasAnyRole("ADMIN", "ATENDENTE")

                        .requestMatchers(HttpMethod.DELETE, "/ordens-servico/**")
                        .hasRole("ADMIN")

                        // Qualquer outro endpoint exige autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}