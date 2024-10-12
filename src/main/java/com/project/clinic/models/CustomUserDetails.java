package com.project.clinic.models;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRoles()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Customize this based on your application's needs
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Customize this based on your application's needs
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Customize this based on your application's needs
    }

    @Override
    public boolean isEnabled() {
        return true; // Customize this based on your application's needs
    }
}
