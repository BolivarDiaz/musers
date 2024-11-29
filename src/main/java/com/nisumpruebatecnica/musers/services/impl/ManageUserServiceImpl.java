package com.nisumpruebatecnica.musers.services.impl;

import com.nisumpruebatecnica.musers.constants.BasicConstants;
import com.nisumpruebatecnica.musers.dto.*;
import com.nisumpruebatecnica.musers.entity.BasicUser;
import com.nisumpruebatecnica.musers.repositories.BasicUserRepository;
import com.nisumpruebatecnica.musers.services.ManageUserService;
import com.nisumpruebatecnica.musers.utils.JwtUtils;
import com.nisumpruebatecnica.musers.utils.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service("ManageUserService")
public class ManageUserServiceImpl implements ManageUserService {

    private BasicUserRepository basicUserRepository;
    private UserUtils userUtils;

    private JwtUtils jwtUtils;

    @Override
    public BasicResponse<SaveUserResponse> saveUser(SaveUserRequest basicUser, boolean isUpdate){
        BasicResponse<SaveUserResponse> result = new BasicResponse<>();
        BasicUser userToStore;
        basicUser.setExistingJwt(jwtUtils.formatToken(basicUser.getExistingJwt()));
        if(userUtils.isValidEmailFormat(basicUser.getEmail(), result) &&
                userUtils.isValidPasswordFormat(basicUser.getPassword(), result)) {

            var verifyUser = isUpdate ? basicUserRepository.findByJwtLike(basicUser.getExistingJwt())
                    : basicUserRepository.findByEmailLike(basicUser.getEmail());

            if (verifyUser != null && !isUpdate) {
                result.setMessage("El correo ya esta registrado");
            } else {
                var newToken = jwtUtils.createToken(basicUser.getUsername());
                userToStore = isUpdate ? userUtils.loadUserToUpdate(basicUser, verifyUser, newToken)
                        : userUtils.loadUserToSave(basicUser, newToken);
                basicUserRepository.save(userToStore);
                var saveUserRs = new SaveUserResponse();
                saveUserRs.setId(userToStore.getId());
                saveUserRs.setCreated(userToStore.getCreated());
                saveUserRs.setModified(userToStore.getModified());
                saveUserRs.setLastLogin(userToStore.getLastLogin());
                saveUserRs.setJwtToken(userToStore.getJwt());
                saveUserRs.setIsActive(userToStore.getIsActive());
                var message = isUpdate ? "User Updated" : "User stored";
                result.setMessage(message);
                result.setData(saveUserRs);
            }
            var statusCode = isUpdate ? HttpStatus.OK : HttpStatus.CREATED;
            result.setStatusCode(statusCode.value());
        }else {
            result.setStatusCode(HttpStatus.BAD_REQUEST.value());
        }
        return result;
    }

    @Override
    public BasicResponse<GetResponse>  getUser(String jwt) {
        jwt = jwtUtils.formatToken(jwt);
        BasicResponse<GetResponse>  result = new BasicResponse<>();
        var response = new SaveUserResponse();
        var user = basicUserRepository.findByJwtLike(jwt);
        if(user != null) {
            result.setData(userUtils.loadUserToShow(user));
        }else {
            result.setMessage("User not found");
        }
        result.setStatusCode(HttpStatus.OK.value());
        return result;
    }

    @Override
    public BasicResponse<SaveUserResponse> deleteUser(String jwt) {
        jwt = jwtUtils.formatToken(jwt);
        BasicResponse<SaveUserResponse>  result = new BasicResponse<>();
        var response = new SaveUserResponse();
        var user = basicUserRepository.findByJwtLike(jwt);
        if(user != null) {
            response.setId(user.getId());
            response.setIsActive(false);
            response.setCreated(user.getCreated());
            response.setLastLogin(user.getLastLogin());
            response.setModified(user.getModified());
            response.setJwtToken(user.getJwt());

            basicUserRepository.delete(user);
            result.setMessage("User Deleted !");
            result.setData(response);
        }else {
            result.setMessage("User not found");
            result.setStatusCode(HttpStatus.OK.value());
        }
        result.setStatusCode(HttpStatus.OK.value());
        return result;
    }

    @Override
    public BasicResponse<LoginResponse> login(LoginRequest loginRequest) {
        BasicResponse<LoginResponse> response = new BasicResponse<>();
        var loginResponse = new LoginResponse();
        var user = basicUserRepository.findByUsernameLike(loginRequest.getUsername());
        if(user != null){
            var encryptedPassword = userUtils.encryptPassword(loginRequest.getPassword());
            if(encryptedPassword.equals(user.getPassword())){
                var jwtToken = jwtUtils.createToken(loginRequest.getUsername());
                user.setLastLogin(new Date());
                user.setJwt(jwtToken);
                basicUserRepository.save(user);
                loginResponse.setLastLogin(user.getLastLogin());
                loginResponse.setJwtToken(jwtToken);
                response.setMessage("Succesful Login");
                response.setData(loginResponse);
            }else{
                response.setMessage("Invalid Password");
            }
        }else{
            response.setMessage("User Not Found");
        }
        response.setStatusCode(HttpStatus.OK.value());
        return response;
    }
    @Override
    public boolean validateToken(String jwt) {
        jwt = jwtUtils.formatToken(jwt);
        var user = basicUserRepository.findByJwtLike(jwt);
        return user != null && jwtUtils.isValidToken(jwt,user.getUsername());
    }
}
