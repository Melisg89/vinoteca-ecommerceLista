package com.tequila.ecommerce.vinoteca.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.tequila.ecommerce.vinoteca.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // ✅ Rutas públicas - Archivos HTML y estáticos
                .requestMatchers(
                    "/", 
                    "/index.html", 
                    "/about.html", 
                    "/product.html", 
                    "/product-single.html",
                    "/cart.html", 
                    "/checkout.html",
                    "/blog.html", 
                    "/blog-single.html",
                    "/contact.html", 
                    "/login.html",
                    "/register.html",
                    "/js/**", 
                    "/css/**", 
                    "/images/**",
                    "/fonts/**"
                ).permitAll()
                
                // ✅ APIs públicas de autenticación
                .requestMatchers("/api/auth/**", "/api/products/**", "/api/categoria/**").permitAll()
                
                // ✅ APIs protegidas para usuarios normales
                .requestMatchers("/api/checkout").authenticated()
                .requestMatchers("/api/orders/**").authenticated()
                
                // ✅ Páginas ADMIN - requieren autenticación (el script del frontend verifica el rol)
                .requestMatchers("/admin-products.html", "/admin-orders.html").permitAll()
                
                // ✅ APIs protegidas solo para ADMIN
                .requestMatchers("/api/admin/**").authenticated()
                
                // ❌ Todo lo demás requiere autenticación
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8081", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
