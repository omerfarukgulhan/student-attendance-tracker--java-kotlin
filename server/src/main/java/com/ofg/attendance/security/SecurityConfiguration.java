package com.ofg.attendance.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    private final TokenFilter tokenFilter;
    private final AuthEntryPoint authEntryPoint;

    @Value("${app.client.host}")
    private String clientHost;

    @Autowired
    public SecurityConfiguration(TokenFilter tokenFilter, AuthEntryPoint authEntryPoint) {
        this.tokenFilter = tokenFilter;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authentication) ->
                        authentication
                                .requestMatchers(HttpMethod.GET, "/qr-codes/{lectureId}").hasAuthority("ROLE_INSTRUCTOR")

                                .requestMatchers(HttpMethod.GET, "/attendances/lecture/{lectureId}").hasAuthority("ROLE_INSTRUCTOR")
                                .requestMatchers(HttpMethod.GET, "/attendances/{attendanceId}").hasAuthority("ROLE_INSTRUCTOR")
                                .requestMatchers(HttpMethod.POST, "/attendances/{qrContent}").hasAuthority("ROLE_STUDENT")

                                .requestMatchers(HttpMethod.GET, "/students").permitAll()
                                .requestMatchers(HttpMethod.GET, "/students/{studentId}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/students").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/students/{studentId}").hasAuthority("ROLE_ADMIN")

                                .requestMatchers(HttpMethod.GET, "/lectures/course/{courseId}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/lectures/{lectureId}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/lectures").hasAuthority("ROLE_INSTRUCTOR")
                                .requestMatchers(HttpMethod.PUT, "/lectures/{lectureId}").hasAuthority("ROLE_INSTRUCTOR")
                                .requestMatchers(HttpMethod.DELETE, "/lectures/{lectureId}").hasAuthority("ROLE_INSTRUCTOR")

                                .requestMatchers(HttpMethod.GET, "/courses").permitAll()
                                .requestMatchers(HttpMethod.GET, "/courses/{courseId}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/courses/instructor/{instructorId}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/courses").hasAuthority("ROLE_INSTRUCTOR")
                                .requestMatchers(HttpMethod.POST, "/courses/assign-student").hasAuthority("ROLE_INSTRUCTOR")
                                .requestMatchers(HttpMethod.PUT, "/courses/{courseId}").hasAuthority("ROLE_INSTRUCTOR")
                                .requestMatchers(HttpMethod.DELETE, "/courses/{courseId}").hasAuthority("ROLE_INSTRUCTOR")

                                .requestMatchers(HttpMethod.GET, "/instructors").permitAll()
                                .requestMatchers(HttpMethod.GET, "/instructors/{instructorId}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/instructors").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/instructors/{instructorId}").hasAuthority("ROLE_INSTRUCTOR")
                                .requestMatchers(HttpMethod.DELETE, "/instructors/{instructorId}").hasAuthority("ROLE_INSTRUCTOR")

                                .requestMatchers(HttpMethod.POST, "/user-roles/assign").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/user-roles/revoke").hasAuthority("ROLE_ADMIN")

                                .requestMatchers(HttpMethod.GET, "/roles").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/roles/{roleId}").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.POST, "/roles").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/roles/{roleId}").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/roles/{roleId}").hasAuthority("ROLE_ADMIN")

                                .requestMatchers(HttpMethod.GET, "/users").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/users/{userId}").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.POST, "/users").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/users/{userId}").hasAuthority("ROLE_USER")
                                .requestMatchers(HttpMethod.PUT, "/users/activate/{token}").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/users/update-password/{userId}").hasAuthority("ROLE_USER")
                                .requestMatchers(HttpMethod.PUT, "/users/reset-password").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/users/reset-password/verify/{token}").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/users/{userId}").hasAuthority("ROLE_USER")

                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                                .anyRequest().permitAll()
                )
                .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(authEntryPoint))
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.disable())
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(corsConfig -> corsConfig.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList(clientHost));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setMaxAge(3600L);
                    return config;
                }));
        return http.build();
    }
}