package com.nisumpruebatecnica.musers.utils;

import com.nisumpruebatecnica.musers.constants.BasicConstants;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";;  // Asegúrate de mantenerla segura

    public String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // 10 horas de expiración
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isValidToken(String token, String username) {
        return username.equals(getUsername(token)) && !isExpiredToken(token);
    }

    private boolean isExpiredToken(String token) {
        return getExpirationDate(token).before(new Date());
    }

    private Date getExpirationDate(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public String formatToken(String token){
            return token != null ? token.replace(BasicConstants.BEARER_CONSTANT,"") : "";
    }
}


