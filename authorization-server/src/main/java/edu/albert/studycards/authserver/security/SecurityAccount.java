package edu.albert.studycards.authserver.security;

import edu.albert.studycards.authserver.domain.interfaces.ClientPersistent;
import edu.albert.studycards.authserver.domain.interfaces.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecurityAccount implements UserDetails {
    
    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean isActive;
    
    public SecurityAccount(String username,
                           String password,
                           List<SimpleGrantedAuthority> authorities,
                           boolean isActive) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }
    
    @Override
    public boolean isEnabled() {
        return isActive;
    }
    
    public static UserDetails fromUser(ClientPersistent client) {
        return new org.springframework.security.core.userdetails.User(
                client.getEmail(),
                client.getPassword(),
                client.getStatus().equals(Status.ACTIVE),
                client.getStatus().equals(Status.ACTIVE),
                client.getStatus().equals(Status.ACTIVE),
                client.getStatus().equals(Status.ACTIVE),
                client.getRole().getAuthorities()
        );
    }
}