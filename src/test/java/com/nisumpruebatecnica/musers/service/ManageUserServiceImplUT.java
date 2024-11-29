package com.nisumpruebatecnica.musers.service;

import com.nisumpruebatecnica.musers.dto.*;
import com.nisumpruebatecnica.musers.entity.BasicUser;
import com.nisumpruebatecnica.musers.repositories.BasicUserRepository;
import com.nisumpruebatecnica.musers.services.impl.ManageUserServiceImpl;
import com.nisumpruebatecnica.musers.utils.JwtUtils;
import com.nisumpruebatecnica.musers.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ManageUserServiceImplUT {

    @Mock
    private BasicUserRepository basicUserRepository;

    @Mock
    private UserUtils userUtils;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private ManageUserServiceImpl manageUserService;

    private SaveUserRequest saveUserRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicializa los mocks antes de cada prueba

        // Inicialización de objetos de prueba
        saveUserRequest = new SaveUserRequest();
        saveUserRequest.setUsername("myuser");
        saveUserRequest.setEmail("myemail@gmail.com");
        loginRequest = new LoginRequest("username", "password");
    }

    // Test para el método saveUser (registro de usuario)
    @Test
    public void testSaveUser_Success() {
        // Datos simulados para el usuario
        BasicUser basicUser = new BasicUser();
        basicUser.setId(5);
        basicUser.setUsername("myuser");
        basicUser.setEmail("myemail@gmail.com");
        basicUser.setJwt("token");
        basicUser.setPassword("mypassword");

        // Simulando la validación del email y la contraseña
        when(userUtils.isValidEmailFormat(Mockito.anyString(), any())).thenReturn(true);
        when(userUtils.isValidPasswordFormat(Mockito.anyString(), any())).thenReturn(true);

        when(basicUserRepository.findByEmailLike(Mockito.anyString())).thenReturn(null);
        when(jwtUtils.createToken(Mockito.anyString())).thenReturn("token");
        when(basicUserRepository.findByJwtLike(Mockito.any())).thenReturn(null);
        when(userUtils.loadUserToSave(saveUserRequest, "token")).thenReturn(basicUser);

        saveUserRequest.setPassword("mypassword");
        // Llamada al método
        BasicResponse<SaveUserResponse> response = manageUserService.saveUser(saveUserRequest, false);

        // Verificación
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertNotNull(response.getData());
        assertEquals("token", response.getData().getJwtToken());
        assertEquals("User stored", response.getMessage());
    }

    // Test para el método saveUser con error en la validación del email
    @Test
    public void testSaveUser_InvalidEmail() {
        // Simulando una validación de email incorrecta
        when(userUtils.isValidEmailFormat(Mockito.anyString(), any())).thenReturn(false);
        saveUserRequest.setEmail("baaaad");
        BasicResponse<SaveUserResponse> response = manageUserService.saveUser(saveUserRequest, false);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    // Test para el método login
    @Test
    public void testLogin_Success() {
        // Datos simulados
        BasicUser user = new BasicUser();
        user.setUsername("username");
        user.setPassword("password");
        user.setJwt("token");

        when(basicUserRepository.findByUsernameLike(loginRequest.getUsername())).thenReturn(user);
        when(userUtils.encryptPassword(loginRequest.getPassword())).thenReturn("password");
        when(jwtUtils.createToken(loginRequest.getUsername())).thenReturn("token");

        BasicResponse<LoginResponse> response = manageUserService.login(loginRequest);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertNotNull(response.getData());
        assertEquals("token", response.getData().getJwtToken());
        assertEquals("Succesful Login", response.getMessage());
    }

    // Test para el método login con contraseña incorrecta
    @Test
    public void testLogin_InvalidPassword() {
        // Datos simulados
        BasicUser user = new BasicUser();
        user.setUsername("username");
        user.setPassword("wrongPassword");

        when(basicUserRepository.findByUsernameLike(loginRequest.getUsername())).thenReturn(user);
        when(userUtils.encryptPassword(loginRequest.getPassword())).thenReturn("validPassword");

        BasicResponse<LoginResponse> response = manageUserService.login(loginRequest);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("Invalid Password", response.getMessage());
    }

    // Test para el método login con usuario no encontrado
    @Test
    public void testLogin_UserNotFound() {
        when(basicUserRepository.findByUsernameLike(loginRequest.getUsername())).thenReturn(null);

        BasicResponse<LoginResponse> response = manageUserService.login(loginRequest);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("User Not Found", response.getMessage());
    }

    // Test para el método getUser
    @Test
    public void testGetUser_Success() {
        String jwtToken = "token";

        BasicUser basicUser = new BasicUser();
        basicUser.setUsername("username");
        basicUser.setEmail("email@example.com");
        GetResponse getResponse = new GetResponse();
        getResponse.setName("myname");
        getResponse.setEmail("myemail@gmail.com");

        when(jwtUtils.formatToken(jwtToken)).thenReturn(jwtToken);
        when(basicUserRepository.findByJwtLike(jwtToken)).thenReturn(basicUser);
        when(userUtils.loadUserToShow(basicUser)).thenReturn(getResponse);

        BasicResponse<GetResponse> response = manageUserService.getUser(jwtToken);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertNotNull(response.getData());
        assertEquals("myname", response.getData().getName());
    }

    // Test para el método getUser con usuario no encontrado
    @Test
    public void testGetUser_UserNotFound() {
        String jwtToken = "token";

        when(jwtUtils.formatToken(jwtToken)).thenReturn(jwtToken);
        when(basicUserRepository.findByJwtLike(jwtToken)).thenReturn(null);

        BasicResponse<GetResponse> response = manageUserService.getUser(jwtToken);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("User not found", response.getMessage());
    }

    // Test para el método deleteUser
    @Test
    public void testDeleteUser_Success() {
        String jwtToken = "token";

        BasicUser basicUser = new BasicUser();
        basicUser.setId(5);
        basicUser.setIsActive(true);
        basicUser.setJwt(jwtToken);

        when(jwtUtils.formatToken(jwtToken)).thenReturn(jwtToken);
        when(basicUserRepository.findByJwtLike(jwtToken)).thenReturn(basicUser);
        doNothing().when(basicUserRepository).delete(basicUser);

        BasicResponse<SaveUserResponse> response = manageUserService.deleteUser(jwtToken);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("User Deleted !", response.getMessage());
    }

    // Test para el método deleteUser con usuario no encontrado
    @Test
    public void testDeleteUser_UserNotFound() {
        String jwtToken = "token";

        when(jwtUtils.formatToken(jwtToken)).thenReturn(jwtToken);
        when(basicUserRepository.findByJwtLike(jwtToken)).thenReturn(null);

        BasicResponse<SaveUserResponse> response = manageUserService.deleteUser(jwtToken);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("User not found", response.getMessage());
    }
}
