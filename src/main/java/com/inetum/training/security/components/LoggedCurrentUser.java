package com.inetum.training.security.components;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LoggedCurrentUser {

    public com.inetum.training.security.model.CurrentUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (com.inetum.training.security.model.CurrentUser) auth.getPrincipal();
    }
}