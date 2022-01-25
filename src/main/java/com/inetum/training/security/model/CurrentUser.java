package com.inetum.training.security.model;

import com.inetum.training.user.domain.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Klasa odpowiedzialna za reprezentacje uzytkownika w kontekscie bezpieczenstwa
 */
@Getter
@Builder
@ToString
@RequiredArgsConstructor
public class CurrentUser implements UserDetails
{

    @NonNull
    private String login;

    @NonNull
    private String passwordHash;

    @NonNull
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(role);
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
