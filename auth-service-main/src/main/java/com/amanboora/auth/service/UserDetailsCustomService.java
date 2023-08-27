package com.amanboora.auth.service;

import com.amanboora.auth.model.AuthUser;
import com.amanboora.auth.reposiroty.UserRepository;
import com.amanboora.auth.security.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsCustomService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final AuthUser userInDb = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("AuthUser '" + username + "' not found"));
        if (userInDb.getActivationId() != null && !userInDb.getActivationId().isEmpty()) {
            return new UserModel(userInDb.getId(), username, userInDb.getPassword(), userInDb.getName(), userInDb.getEmail(), Collections.emptySet(), false);
        } else {
            return new UserModel(userInDb.getId(), username, userInDb.getPassword(), userInDb.getName(), userInDb.getEmail(), Collections.emptySet(), true);
        }
    }
}
