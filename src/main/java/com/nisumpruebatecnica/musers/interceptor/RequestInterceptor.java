package com.nisumpruebatecnica.musers.interceptor;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nisumpruebatecnica.musers.dto.BasicResponse;
import com.nisumpruebatecnica.musers.services.ManageUserService;
import com.nisumpruebatecnica.musers.services.impl.ManageUserServiceImpl;
import com.nisumpruebatecnica.musers.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class RequestInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestInterceptor.class);
    private static final String CONTENT_TYPE = "application/json";
    private ManageUserServiceImpl manageUserServiceImpl;

    @Autowired
    public RequestInterceptor(ManageUserServiceImpl manageUserServiceImpl) {
        this.manageUserServiceImpl = manageUserServiceImpl;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        BasicResponse basicResponse = new BasicResponse<>();
        var objectMapper = new ObjectMapper();
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            var token = request.getHeader("AUTHORIZATION");
            var isValidToken = false;
            if (token != null && !token.isEmpty())
                isValidToken =  manageUserServiceImpl.validateToken(token);

            if(!isValidToken){
                response.setContentType(CONTENT_TYPE);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                basicResponse.setMessage("Jwt Invalido");
                basicResponse.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(objectMapper.writeValueAsString(basicResponse));
            }
            return isValidToken;
        }catch (Exception e) {
            LOGGER.info("{}", e.getMessage());
            response.setContentType(CONTENT_TYPE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            basicResponse.setMessage(e.getMessage());
            basicResponse.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(basicResponse));
            return false;
        }
    }

}