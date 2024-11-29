package com.nisumpruebatecnica.musers.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BasicConstants {
    public static final List<String> WHITELIST = Collections.unmodifiableList(Arrays.asList(
            "/v3/api-docs/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-resources/**",
            "/swagger-ui/index.html",
            "/swagger-ui.index.html",
            "/swagger-ui/**",
            "/v3/api-docs/swagger-config/**",
            "/muser/v1/login",
            "/muser/v1/registeruser"));

    public static final String BEARER_CONSTANT = "Bearer ";
}
