package com.nisumpruebatecnica.musers.controllers;


import com.nisumpruebatecnica.musers.dto.*;
import com.nisumpruebatecnica.musers.entity.BasicUser;
import com.nisumpruebatecnica.musers.services.ManageUserService;
import com.nisumpruebatecnica.musers.services.impl.ManageUserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/muser/v1")
@RequiredArgsConstructor
@Tag(name = "Gestión de Usuarios", description = "Operaciones relacionadas con la gestión de usuarios.")
public class ManageUserController {
    @Autowired
    private ManageUserService manageUserService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Este endpoint permite a un usuario iniciar sesión proporcionando su nombre de usuario y contraseña.")
    public ResponseEntity<BasicResponse<LoginResponse> > login(
            @Parameter(description = "Datos de inicio de sesión del usuario", required = true)
            @RequestBody LoginRequest loginRequest){
        BasicResponse<LoginResponse> basicUserBasicResponse = new BasicResponse<>();
        try {
            basicUserBasicResponse = manageUserService.login(loginRequest);
        }catch (Exception e){
            basicUserBasicResponse.setMessage("Error intentando hacer login");
            basicUserBasicResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(basicUserBasicResponse,
                HttpStatus.valueOf(basicUserBasicResponse.getStatusCode()));
    }

    @PostMapping("/registeruser")
    @Operation(summary = "Registrar usuario", description = "Este endpoint permite registrar un nuevo usuario en el sistema.")
    public ResponseEntity<BasicResponse<SaveUserResponse> > saveUser(
            @Parameter(description = "Información del usuario a registrar", required = true)
            @RequestBody SaveUserRequest saveUserRequest){
        BasicResponse<SaveUserResponse> basicUserBasicResponse = new BasicResponse<>();
        try {
            basicUserBasicResponse = manageUserService.saveUser(saveUserRequest, false);
        }catch (Exception e){
            basicUserBasicResponse.setMessage("Error intentando guarrdar Usuario");
            basicUserBasicResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(basicUserBasicResponse,
                HttpStatus.valueOf(basicUserBasicResponse.getStatusCode()));
    }

    @GetMapping("/getuser")
    @Operation(summary = "Obtener usuario", description = "Este endpoint obtiene la información de un usuario usando su JWT.")
    public ResponseEntity<BasicResponse<GetResponse> > getUser(
            @Parameter(description = "Token JWT del usuario", required = true)
            @RequestHeader(value = "Authorization", required = true) String jwtToken){
        BasicResponse<GetResponse> basicUserBasicResponse = new BasicResponse<>();
        try {
            basicUserBasicResponse = manageUserService.getUser(jwtToken);
        }catch (Exception e){
            basicUserBasicResponse.setMessage("Error intentando obtener Usuario");
            basicUserBasicResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(basicUserBasicResponse,
                HttpStatus.valueOf(basicUserBasicResponse.getStatusCode()));
    }

    @PostMapping("/updateuser")
    @Operation(summary = "Actualizar usuario", description = "Este endpoint permite actualizar la información de un usuario ya registrado.")
    public ResponseEntity<BasicResponse<SaveUserResponse>> updateUser(
            @Parameter(description = "Token JWT del usuario", required = true) @RequestHeader(value = "Authorization", required = true) String jwtToken,
            @Parameter(description = "Información actualizada del usuario", required = true) @RequestBody  SaveUserRequest saveUserRequest){
        BasicResponse<SaveUserResponse> basicUserBasicResponse = new BasicResponse<>();
        try {
            saveUserRequest.setExistingJwt(jwtToken);
            basicUserBasicResponse = manageUserService.saveUser(saveUserRequest, true);
        }catch (Exception e){
            basicUserBasicResponse.setMessage("Error intentando actualizar Usuario");
            basicUserBasicResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(basicUserBasicResponse,
                HttpStatus.valueOf(basicUserBasicResponse.getStatusCode()));
    }

    @PostMapping("/deleteuser")
    @Operation(summary = "Eliminar usuario", description = "Este endpoint permite eliminar un usuario usando su JWT.")
    public ResponseEntity<BasicResponse<SaveUserResponse>> deleteUser(
            @Parameter(description = "Token JWT del usuario", required = true) @RequestHeader(value = "Authorization", required = true) String jwtToken){
        BasicResponse<SaveUserResponse> basicUserBasicResponse = new BasicResponse<>();
        try {
            basicUserBasicResponse = manageUserService.deleteUser(jwtToken);
        }catch (Exception e){
            basicUserBasicResponse.setMessage("Error intentando borrar Usuario");
            basicUserBasicResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(basicUserBasicResponse,
                HttpStatus.valueOf(basicUserBasicResponse.getStatusCode()));
    }



}
