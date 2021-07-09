package com.inetum.training.security.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Klasa odpowiedzialna za reprezentacje uzytkownika w kontekscie bezpieczenstwa
 */
@Getter
@Builder
@ToString
@RequiredArgsConstructor
public class CurrentUser //implements UserDetails
{

    @NonNull
    private String login;

    @NonNull
    private String passwordHash;

    @NonNull
    private String role;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return AuthorityUtils.createAuthorityList(role);
//    }
}
