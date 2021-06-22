package edu.albert.studycards.resourceserver.model.interfaces;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static edu.albert.studycards.resourceserver.model.interfaces.Permission.*;


public enum Role {
    USER(Set.of(USER_READ, USER_WRITE, USER_UPDATE, USER_DELETE)),
    ADMIN(Set.of(USER_READ, USER_WRITE, USER_UPDATE, USER_DELETE,
        DEVELOPER_READ, DEVELOPER_WRITE, DEVELOPER_UPDATE, DEVELOPER_DELETE));
    
    private final Set<Permission> permissions;
    
    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }
    
    public Set<Permission> getPermissions() {
        return permissions;
    }
    
    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                   .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                   .collect(Collectors.toSet());
    }
}
