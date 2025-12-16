package com.tequila.ecommerce.vinoteca.security;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret:miClaveSecretaMuySeguraParaJWTPorFavor123456}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(Long userId, String email, String role, String nombre) {
        logger.info("🔐 GENERANDO JWT TOKEN");
        logger.info("   📌 User ID: {}", userId);
        logger.info("   📧 Email: {}", email);
        logger.info("   👤 Nombre: {}", nombre);
        logger.info("   🛡️  Role: {}", role);

        String token = Jwts.builder()
            .setSubject(userId.toString())
            .claim("email", email)
            .claim("role", role)
            .claim("nombre", nombre)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 86400000))
            .signWith(getSigningKey())
            .compact();

        logger.info("✅ JWT generado exitosamente");
        logger.info("   🎫 Token (primeros 50 chars): {}", token.substring(0, Math.min(50, token.length())) + "...");
        
        return token;
    }

    private String createToken(Map<String, Object> claims, String subject, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .claim("email", email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserIdFromToken(String token) {
        return Long.parseLong(getAllClaimsFromToken(token).getSubject());
    }

    public String getRoleFromToken(String token) {
        String role = (String) getAllClaimsFromToken(token).get("role");
        logger.info("🔍 Role extraído del token: {}", role);
        return role;
    }

    public String getEmailFromToken(String token) {
        return (String) getAllClaimsFromToken(token).get("email");
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            logger.info("✅ Token válido");
            logger.info("   📌 Claims: userId={}, email={}, role={}", 
                claims.getSubject(), 
                claims.get("email"), 
                claims.get("role"));
            return true;
        } catch (Exception e) {
            logger.error("❌ Token inválido: {}", e.getMessage());
            return false;
        }
    }

    public boolean isAdmin(String token) {
        try {
            String role = getRoleFromToken(token);
            boolean isAdmin = "ADMIN".equalsIgnoreCase(role);
            logger.info("🔐 Verificando si es ADMIN: {} -> {}", role, isAdmin ? "✅ SÍ" : "❌ NO");
            return isAdmin;
        } catch (Exception e) {
            logger.error("❌ Error verificando si es admin: {}", e.getMessage());
            return false;
        }
    }
}
