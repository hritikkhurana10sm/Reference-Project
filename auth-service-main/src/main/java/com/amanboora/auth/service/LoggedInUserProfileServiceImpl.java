package com.amanboora.auth.service;

import com.amanboora.auth.api.dao.LoggedInUserProfileService;
import com.amanboora.auth.api.dto.ProfileDto;
import com.amanboora.auth.api.exception.AuthException;
import com.amanboora.auth.api.generic.Constants;
import com.amanboora.auth.model.AuthUser;
import com.amanboora.auth.reposiroty.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LoggedInUserProfileServiceImpl implements LoggedInUserProfileService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ProfileDto getUserProfile(String id) {
        AuthUser authUser = userRepository.findById(id)
                .orElseThrow(() -> new AuthException(Constants.USER_NOTFOUND, HttpStatus.INTERNAL_SERVER_ERROR));
        return new ProfileDto(authUser.getId(), authUser.getUsername(), authUser.getName(), authUser.getEmail());
    }
}
