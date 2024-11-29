package com.nisumpruebatecnica.musers.controller;

import com.nisumpruebatecnica.musers.controllers.ManageUserController;
import com.nisumpruebatecnica.musers.dto.*;
import com.nisumpruebatecnica.musers.services.ManageUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ManageUserControllerUT {

    @Mock
    private ManageUserService manageUserService;

    @InjectMocks
    private ManageUserController manageUserController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicializa los mocks antes de cada prueba
    }

    // Test para el método login
    @Test
    public void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        LoginResponse loginResponse = new LoginResponse("token", new Date());
        BasicResponse<LoginResponse> basicResponse = new BasicResponse<>();
        basicResponse.setData(loginResponse);
        basicResponse.setStatusCode(HttpStatus.OK.value());

        when(manageUserService.login(loginRequest)).thenReturn(basicResponse);

        ResponseEntity<BasicResponse<LoginResponse>> response = manageUserController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("token", response.getBody().getData().getJwtToken());
    }

    // Test para el método login con error
    @Test
    public void testLogin_Error() {
        LoginRequest loginRequest = new LoginRequest("username", "wrongPassword");
        BasicResponse<LoginResponse> basicResponse = new BasicResponse<>();
        basicResponse.setMessage("Error intentando hacer login");
        basicResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        when(manageUserService.login(loginRequest)).thenThrow(new RuntimeException("Login failed"));

        ResponseEntity<BasicResponse<LoginResponse>> response = manageUserController.login(loginRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error intentando hacer login", response.getBody().getMessage());
    }

    // Test para el método saveUser (registro de usuario)
    @Test
    public void testSaveUser_Success() {
        SaveUserRequest saveUserRequest = new SaveUserRequest();
        saveUserRequest.setUsername("myuser");
        saveUserRequest.setPassword("mypassword");
        SaveUserResponse saveUserResponse = new SaveUserResponse();
        saveUserResponse.setJwtToken("myjwttoken");
        saveUserResponse.setModified(new Date());
        BasicResponse<SaveUserResponse> basicResponse = new BasicResponse<>();
        basicResponse.setData(saveUserResponse);
        basicResponse.setStatusCode(HttpStatus.CREATED.value());

        when(manageUserService.saveUser(saveUserRequest, false)).thenReturn(basicResponse);

        ResponseEntity<BasicResponse<SaveUserResponse>> response = manageUserController.saveUser(saveUserRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("myjwttoken", response.getBody().getData().getJwtToken());
    }

    // Test para el método saveUser con error
    @Test
    public void testSaveUser_Error() {
        SaveUserRequest saveUserRequest = new SaveUserRequest();
        saveUserRequest.setUsername("myuser");
        saveUserRequest.setPassword("mypassword");
        BasicResponse<SaveUserResponse> basicResponse = new BasicResponse<>();
        basicResponse.setMessage("Error saving user");
        basicResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        when(manageUserService.saveUser(saveUserRequest, false)).thenThrow(new RuntimeException("Error intentando guarrdar Usuario"));

        ResponseEntity<BasicResponse<SaveUserResponse>> response = manageUserController.saveUser(saveUserRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error intentando guarrdar Usuario", response.getBody().getMessage());
    }

    // Test para el método getUser
    @Test
    public void testGetUser_Success() {
        String jwtToken = "token";
        GetResponse getResponse = new GetResponse();
        getResponse.setIsActive(true);
        getResponse.setName("myuser");

        BasicResponse<GetResponse> basicResponse = new BasicResponse<>();
        basicResponse.setData(getResponse);
        basicResponse.setStatusCode(HttpStatus.OK.value());

        when(manageUserService.getUser(jwtToken)).thenReturn(basicResponse);

        ResponseEntity<BasicResponse<GetResponse>> response = manageUserController.getUser(jwtToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getData().getIsActive());
    }

    // Test para el método getUser con error
    @Test
    public void testGetUser_Error() {
        String jwtToken = "token";
        BasicResponse<GetResponse> basicResponse = new BasicResponse<>();
        basicResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        when(manageUserService.getUser(jwtToken)).thenThrow(new RuntimeException("Error getting user"));

        ResponseEntity<BasicResponse<GetResponse>> response = manageUserController.getUser(jwtToken);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error intentando obtener Usuario", response.getBody().getMessage());
    }

    // Test para el método updateUser
    @Test
    public void testUpdateUser_Success() {
        String jwtToken = "token";
        SaveUserRequest saveUserRequest = new SaveUserRequest();
        saveUserRequest.setName("newuser");
        saveUserRequest.setPassword("newpassword");
        SaveUserResponse saveUserResponse = new SaveUserResponse();
        saveUserResponse.setId(5);
        BasicResponse<SaveUserResponse> basicResponse = new BasicResponse<>();
        basicResponse.setData(saveUserResponse);
        basicResponse.setStatusCode(HttpStatus.OK.value());

        when(manageUserService.saveUser(saveUserRequest, true)).thenReturn(basicResponse);
        ResponseEntity<BasicResponse<SaveUserResponse>> response = manageUserController.updateUser(jwtToken, saveUserRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody().getData().getId());
    }

    // Test para el método deleteUser
    @Test
    public void testDeleteUser_Success() {
        String jwtToken = "token";
        SaveUserResponse saveUserResponse = new SaveUserResponse();
        saveUserResponse.setIsActive(true);
        saveUserResponse.setId(5);
        BasicResponse<SaveUserResponse> basicResponse = new BasicResponse<>();
        basicResponse.setData(saveUserResponse);
        basicResponse.setStatusCode(HttpStatus.OK.value());

        when(manageUserService.deleteUser(jwtToken)).thenReturn(basicResponse);

        ResponseEntity<BasicResponse<SaveUserResponse>> response = manageUserController.deleteUser(jwtToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody().getData().getId());
    }
}
