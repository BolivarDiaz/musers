package com.nisumpruebatecnica.musers.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


public class JwtUtilsUT {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Value("${secret.jwt.key}")
    private String SECRET_KEY = "testSecretKey";

    private String validToken;
    private String invalidToken;
    private String username = "testUser";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks

        // Crear un token válido de prueba
        validToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFucm9kcmlndWV6IiwiaWF0IjoxNzMyODg4Njg0LCJleHAiOjE3MzI5MjQ2ODR9.cpMAjV-TEcrHGRbmWJU3Vh8vPSOLRdADl6jCG_fw3cI";

        // Crear un token inválido (expirado)
        invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFucm9kcmlndWV6IiwiaWF0IjoxNzMyODg4Njg0LCJleHAiOjE3MzI5MjQ2ODR9.cpMAjV-TEcrHGRbmWJU3Vh8vPSOLRdADl6jCG_fw3cI";
    }


    // Test para el método formatToken
    @Test
    public void testFormatToken() {
        String token = "Bearer " + validToken;
        String formattedToken = jwtUtils.formatToken(token);

        assertEquals(validToken, formattedToken);
    }

    // Test para el método formatToken con token null
    @Test
    public void testFormatToken_Null() {
        String formattedToken = jwtUtils.formatToken(null);

        assertEquals("", formattedToken);
    }

    // Test para el método formatToken sin prefijo 'Bearer'
    @Test
    public void testFormatToken_NoBearer() {
        String token = validToken;
        String formattedToken = jwtUtils.formatToken(token);

        assertEquals(validToken, formattedToken);
    }
}
