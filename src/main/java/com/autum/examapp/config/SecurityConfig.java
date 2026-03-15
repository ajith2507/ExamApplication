package com.autum.examapp.config;

import com.autum.examapp.security.JwtFilter;
import com.autum.examapp.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public JwtFilter jwtFilter(){
        return new JwtFilter(jwtUtil);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})

                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(
                                "/",
                                "/index.html",
                                "/favicon.ico",
                                "/**/*.js",
                                "/**/*.css",
                                "/**/*.html",
                                "/**/*.png",
                                "/**/*.jpg",
                                "/**/*.svg"
                        ).permitAll()



                        /* PUBLIC APIs */
                                .requestMatchers(
                                "/users/login",
                                "/users/save",
                                "/users/verify-otp",
                                "/users/resend-otp",
                                "/users/forgot-password",
                                "/users/reset-password",
                                        "/users/verify-reset-otp"
                                ).permitAll()
                        /* ADMIN + MAIN ADMIN */
                                .requestMatchers(
                                        "/quiz/create",
                                 "/quiz/category/**",
                                 "/question/add",
                                 "/questions/deletequestion/**",
                                 "/questions/admin/**",
                                 "/admin/create-admin",
                                 "/admin/delete-admin/**",
                                 "/admin/all-admins","/admin/admin/all","/quiz/deletequiz/**"
                                 ).hasAnyRole("ADMIN","MAIN_ADMIN")
                                 .requestMatchers(
                                 "/quiz/**",
                                 "/questions/quiz/**",
                                 "/attempt/**",
                                 "/users/profile",
                                         "/questions/attempts/my"
                                 ).hasAuthority("ROLE_USER")

                                 .anyRequest().permitAll()
                                 )

                               .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /* PASSWORD ENCODER */

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /* CORS CONFIG */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


}