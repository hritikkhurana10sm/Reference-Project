package com.amanboora.auth.security.util;

import com.amanboora.auth.api.exception.AuthException;
import com.amanboora.auth.api.generic.Constants;
import com.amanboora.auth.security.model.UserModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class PrincipalDetails {

    public UserModel getPrincipalDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return (UserModel) auth.getPrincipal();
        } else {
            throw new AuthException(Constants.AUTH_FAILED_MESSAGE, HttpStatus.FORBIDDEN);
        }
    }
}
