package com.amanboora.auth.api.dao;

import com.amanboora.auth.api.dto.ProfileDto;
import com.amanboora.auth.api.generic.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface LoggedInUserController {

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Response<ProfileDto>> getUserProfile();
}
