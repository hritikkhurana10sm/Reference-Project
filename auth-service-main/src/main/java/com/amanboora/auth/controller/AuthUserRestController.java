package com.amanboora.auth.controller;

import com.amanboora.auth.api.dao.AuthUserController;
import com.amanboora.auth.api.dao.AuthUserService;
import com.amanboora.auth.api.dto.ResetPasswordDto;
import com.amanboora.auth.api.dto.SignupDto;
import com.amanboora.auth.api.exception.AuthException;
import com.amanboora.auth.api.generic.Constants;
import com.amanboora.auth.api.generic.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AuthUserRestController implements AuthUserController {

    private final AuthUserService authUserService;

    @Autowired
    public AuthUserRestController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @Override
    public ResponseEntity<Response<Void>> signupUser(SignupDto signupDto) {
        try {
            authUserService.signup(signupDto);
            List<String> messages = new ArrayList<>();
            messages.add(Constants.USER_REGISTERED);
            Response<Void> response = new Response<>(null, messages);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataIntegrityViolationException ex) {
            throw new AuthException(ex.getMostSpecificCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Response<String>> signinUser(MultiValueMap<String, Object> encodedData) throws UnsupportedEncodingException {
        String accessToken = authUserService.signin(encodedData);
        List<String> messages = new ArrayList<>();
        messages.add(Constants.LOGIN_SUCCESSFULLY);
        Response<String> response = new Response<>(accessToken, messages);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response<Void>> activateUser(String userId, String activationId) {
        authUserService.activateUser(userId, activationId);
        List<String> messages = new ArrayList<>();
        messages.add(Constants.USER_ACTIVATED);
        Response<Void> response = new Response<>(null, messages);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response<Void>> initiateResetPassword(String usernameOrEmail) {
        authUserService.initiateResetPassword(usernameOrEmail);
        List<String> messages = new ArrayList<>();
        messages.add("Email have been sent to registered email-address");
        Response<Void> response = new Response<>(null, messages);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response<Void>> resetPassword(String userId, String resetId, ResetPasswordDto resetPasswordDto) {
        this.authUserService.resetPassword(userId, resetId, resetPasswordDto);
        List<String> messages = new ArrayList<>();
        messages.add("Password Reset Successfully");
        return new ResponseEntity<>(new Response<>(null, messages), HttpStatus.OK);
    }
}