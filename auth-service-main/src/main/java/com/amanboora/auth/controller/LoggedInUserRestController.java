package com.amanboora.auth.controller;

import com.amanboora.auth.api.dao.LoggedInUserController;
import com.amanboora.auth.api.dao.LoggedInUserProfileService;
import com.amanboora.auth.api.dto.ProfileDto;
import com.amanboora.auth.api.generic.Response;
import com.amanboora.auth.security.util.PrincipalDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class LoggedInUserRestController implements LoggedInUserController {

    private final LoggedInUserProfileService loggedInUserProfileService;
    private final PrincipalDetails principalDetails;

    @Autowired
    public LoggedInUserRestController(LoggedInUserProfileService loggedInUserProfileService, PrincipalDetails principalDetails) {
        this.loggedInUserProfileService = loggedInUserProfileService;
        this.principalDetails=principalDetails;
    }

    @Override
    public ResponseEntity<Response<ProfileDto>> getUserProfile() {
        String userIdFromToken=this.principalDetails.getPrincipalDetails().getId();
        ProfileDto userProfile=this.loggedInUserProfileService.getUserProfile(userIdFromToken);
        Response<ProfileDto> response=new Response<>(userProfile,new ArrayList<>());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
