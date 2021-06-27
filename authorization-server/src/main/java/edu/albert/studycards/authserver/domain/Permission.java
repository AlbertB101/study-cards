package edu.albert.studycards.authserver.domain;

public enum Permission {
    USER_WRITE("user:write"),
    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete"),
    DEVELOPER_READ("developer:read"),
    DEVELOPER_WRITE("developer:write"),
    DEVELOPER_UPDATE("developer:update"),
    DEVELOPER_DELETE("developer:delete");
    
    private final String permission;
    
    Permission(String permission) {
        this.permission = permission;
    }
    
    public String getPermission() {
        return permission;
    }
}
