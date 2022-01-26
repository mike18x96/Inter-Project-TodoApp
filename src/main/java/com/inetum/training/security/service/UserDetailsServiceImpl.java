package com.inetum.training.security.service;

import com.inetum.training.security.model.CurrentUser;
import com.inetum.training.user.domain.User;
import com.inetum.training.user.persistance.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private CurrentUser currentUser;
    private User user;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (userRepository.findById(username) == null) {
            throw new UsernameNotFoundException("No found user with login: " + username);
        } else {
            user = userRepository.findById(username).get();
            currentUser = CurrentUser.builder()
                    .login(user.getLogin())
                    .passwordHash(user.getPasswordHash())
                    .role("ROLE_" + user.getRole())
                    .build();
        }
        return currentUser;
    }
}

