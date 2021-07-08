package com.inetum.training.user.service;

import com.inetum.training.user.domain.User;
import com.inetum.training.user.persistance.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@Slf4j
public class UserInitializer {

    @Autowired
    public UserInitializer(UserRepository repository){
        User admin = User.builder().login("admin")
                .passwordHash("admin")
                .role(User.Role.ADMIN)
                .build();
        User plain = User.builder().login("henio")
                .passwordHash("henio")
                .role(User.Role.USER)
                .build();
        Stream.of( admin, plain)
                .forEach(user -> {
                    repository.save(user);
                    log.info("user added: {}", user);
                });
    }
}
