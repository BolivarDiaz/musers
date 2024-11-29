package com.nisumpruebatecnica.musers.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nisumpruebatecnica.musers.dto.BasicResponse;
import com.nisumpruebatecnica.musers.dto.GetResponse;
import com.nisumpruebatecnica.musers.dto.Phone;
import com.nisumpruebatecnica.musers.dto.SaveUserRequest;
import com.nisumpruebatecnica.musers.entity.BasicUser;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class UserUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserUtils.class);

    @Value("${secret.encrypt.key}")
    private String encryptKey;

    @Value("${regex.email.validation}")
    private String emailRegex;

    @Value("${regex.password.validation}")
    private String passwordRegex;

    public String encryptPassword(String password){
        SecretKeySpec clave = new SecretKeySpec(encryptKey.getBytes(), "AES");
        var passEncrypted = "";
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, clave);
            byte[] textoEncriptado = cipher.doFinal(password.getBytes());
            passEncrypted = Base64.getEncoder().encodeToString(textoEncriptado);
        }catch (Exception e){
            LOGGER.info("Error durante la encriptacion del password");
        }
        return passEncrypted;
    }

    public BasicUser loadUserToSave(SaveUserRequest saveUserRequest, String jwt){
        var basicUser = new BasicUser();
        try {
            ObjectMapper mapper = new ObjectMapper();
            basicUser.setName(saveUserRequest.getName());
            basicUser.setUsername(saveUserRequest.getUsername());
            basicUser.setEmail(saveUserRequest.getEmail());
            basicUser.setPassword(encryptPassword(saveUserRequest.getPassword()));
            basicUser.setJsonPhones(mapper.writeValueAsString(saveUserRequest.getPhones()));
            basicUser.setCreated(new Date());
            basicUser.setModified(new Date());
            basicUser.setLastLogin(new Date());
            basicUser.setIsActive(true);
            basicUser.setJwt(jwt);
        }catch (Exception e){
            LOGGER.info("Error loadUserToSave",e);
        }
        return basicUser;
    }


    public BasicUser loadUserToUpdate(SaveUserRequest saveUserRequest, BasicUser existUser, String jwt){
        var basicUser = existUser;
        try {
            ObjectMapper mapper = new ObjectMapper();
            basicUser.setName(saveUserRequest.getName());
            basicUser.setUsername(saveUserRequest.getUsername());
            basicUser.setEmail(saveUserRequest.getEmail());
            basicUser.setPassword(encryptPassword(saveUserRequest.getPassword()));
            basicUser.setJsonPhones(mapper.writeValueAsString(saveUserRequest.getPhones()));
            basicUser.setModified(new Date());
            basicUser.setIsActive(true);
            basicUser.setJwt(jwt);
        }catch (Exception e){
            LOGGER.info("Error updating User",e);
        }
        return basicUser;
    }

    public GetResponse loadUserToShow(BasicUser basicUser){
        var response = new GetResponse();
        try {
            var mapper = new ObjectMapper();
            List<Phone> listPhones = mapper.readValue(basicUser.getJsonPhones(), List.class);
            response.setPhones(listPhones);
            response.setName(basicUser.getName());
            response.setPassword(basicUser.getPassword());
            response.setEmail(basicUser.getEmail());
            response.setIsActive(basicUser.getIsActive());
            response.setCreationDate(basicUser.getCreated());
            response.setLastLogin(basicUser.getLastLogin());
        }catch (Exception e){
            LOGGER.info("Error loadUserToShow",e);
        }
        return response;
    }

    public boolean isValidPasswordFormat(String password, BasicResponse<?> basicResponse){
        var pattern = Pattern.compile(passwordRegex);
        var isValidPassword = pattern.matcher(password).matches();
        if(!isValidPassword && StringUtils.isEmpty(basicResponse.getMessage()))
            basicResponse.setMessage("El formato de la clave es invalido");

        return isValidPassword;
    }

    public boolean isValidEmailFormat(String email, BasicResponse<?> basicResponse){
        var pattern = Pattern.compile(emailRegex);
        var isValidEmail = pattern.matcher(email).matches();
        if(!isValidEmail)
            basicResponse.setMessage("El formato del email es invalido");

        return isValidEmail;
    }

}
