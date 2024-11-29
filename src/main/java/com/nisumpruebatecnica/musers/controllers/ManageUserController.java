package com.nisumpruebatecnica.musers.controllers;


import com.nisumpruebatecnica.musers.dto.*;
import com.nisumpruebatecnica.musers.entity.BasicUser;
import com.nisumpruebatecnica.musers.services.ManageUserService;
import com.nisumpruebatecnica.musers.services.impl.ManageUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/muser/v1")
@RequiredArgsConstructor
public class ManageUserController {

    @Autowired
    private ManageUserService manageUserService;

    @PostMapping("/login")
    public ResponseEntity<BasicResponse<LoginResponse> > login(@RequestBody LoginRequest loginRequest){
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
    public ResponseEntity<BasicResponse<SaveUserResponse> > saveUser(@RequestBody SaveUserRequest saveUserRequest){
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
    public ResponseEntity<BasicResponse<GetResponse> > getUser(@RequestHeader(value = "Authorization", required = true) String jwtToken){
        BasicResponse<GetResponse> basicUserBasicResponse = new BasicResponse<>();
        try {
            basicUserBasicResponse = manageUserService.getUser(jwtToken);
        }catch (Exception e){
            basicUserBasicResponse.setMessage("Error intentando guarrdar Usuario");
            basicUserBasicResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseEntity<>(basicUserBasicResponse,
                HttpStatus.valueOf(basicUserBasicResponse.getStatusCode()));
    }

    @PostMapping("/updateuser")
    public ResponseEntity<BasicResponse<SaveUserResponse>> updateUser(@RequestHeader(value = "Authorization", required = true) String jwtToken,
                                                                    @RequestBody  SaveUserRequest saveUserRequest){
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
    public ResponseEntity<BasicResponse<SaveUserResponse>> deleteUser(@RequestHeader(value = "Authorization", required = true) String jwtToken){
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
