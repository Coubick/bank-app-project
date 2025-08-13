package org.example.security;

import jakarta.servlet.http.HttpServletResponse;
import org.example.services.GatewayUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final GatewayUserService userDetailsService;
    private final JwtCore jwtCore;

    public SecurityConfiguration(GatewayUserService userDetailsService, JwtCore jwtCore) {
        this.userDetailsService = userDetailsService;
        this.jwtCore = jwtCore;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CustomFilter customFilter = new CustomFilter(jwtCore, userDetailsService);

        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.addAllowedOrigin("*");
                    config.addAllowedMethod("*");
                    config.addAllowedHeader("*");
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/signin").permitAll()
                        .requestMatchers(
                                "/api/gateway/admin/create-user",
                                "/api/gateway/admin/get-all-accounts",
                                "/api/gateway/admin/get-accounts-by-login/{login}",
                                "/api/gateway/admin/filter",
                                "/api/gateway/admin/get-info-about-user/{login}")
                        .hasRole("ADMIN")
                        .requestMatchers("/api/gateway/users/info-about-client",
                                "/api/gateway/client/add-friend/{friendsLogin}",
                                "/api/gateway/client/delete-friend/{friendsLogin}",
                                "/api/gateway/client/my-accounts",
                                "/api/gateway/client/my-account-by-id/{id}",
                                "/api/gateway/client/my-account/{id}/deposit",
                                "/api/gateway/client/my-account/{id}/withdraw",
                                "/api/gateway/client/{senderAccountId}/transfer/{receiverAccountId}",
                                "/api/gateway/client/friends-with-accounts")
                        .hasRole("CLIENT")
                        .anyRequest().authenticated()


                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write(authException.getMessage()
                            );
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                    response.getWriter().write(accessDeniedException.getMessage());
                                }
                        )
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(
                                ClearSiteDataHeaderWriter.Directive.COOKIES,
                                ClearSiteDataHeaderWriter.Directive.STORAGE)))
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.getWriter().write("Logout successful");
                        })
                        .deleteCookies("JSESSIONID")
                )
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}