package com.inetum.training.security;

import com.inetum.training.entity.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String ROLE_PREFIX = "ROLE_";

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .map(user -> CurrentUser.builder()
                        .id(user.getId())
                        .login(user.getLogin())
                        .passwordHash(user.getPasswordHash())
                        .role(ROLE_PREFIX + user.getRole().name())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
