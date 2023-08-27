package com.amanboora.auth.api.dao;

import com.amanboora.auth.api.dto.ProfileDto;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public interface LoggedInUserProfileService extends Serializable {

    ProfileDto getUserProfile(String id);
}
