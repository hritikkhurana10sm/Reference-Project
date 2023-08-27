package com.amanboora.parking.services;

import com.amanboora.auth.security.util.PrincipalDetails;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope
@Getter
@Setter
public class UserServices {

    private String userId;

    @Autowired
    private PrincipalDetails principalDetails;

    @PostConstruct
    public void init() {
        this.userId= this.principalDetails.getPrincipalDetails().getId();
    }
}
