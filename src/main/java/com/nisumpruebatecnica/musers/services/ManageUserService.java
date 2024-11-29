package com.nisumpruebatecnica.musers.services;

import com.nisumpruebatecnica.musers.dto.*;
import com.nisumpruebatecnica.musers.entity.BasicUser;

public interface ManageUserService {

    BasicResponse<SaveUserResponse> saveUser(SaveUserRequest basicUser, boolean isUpdate);
    BasicResponse<GetResponse> getUser(String jwt);

    BasicResponse<SaveUserResponse> deleteUser(String jwt);

    BasicResponse<LoginResponse> login(LoginRequest loginRequest);

    boolean validateToken(String jwt);


}
