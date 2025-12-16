package com.tequila.ecommerce.vinoteca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", "/index.html", "/about.html", "/product.html", "/product-single.html",
                    "/cart.html", "/blog.html", "/contact.html", "/checkout.html", // checkout.html es público
                    "/js/**", "/css/**", "/images/**",
                    "/api/products/**", "/api/categoria/**", "/api/auth/**", "/register", "/login"
                ).permitAll()
                .requestMatchers("/checkout", "/checkout/**").authenticated() // Solo el endpoint REST requiere login
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
