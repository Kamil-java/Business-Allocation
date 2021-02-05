package pl.bak.businessallocationapp.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_BOSS,
    ROLE_EMPLOYEE,
    ROLE_MANAGER;

    @Override
    public String getAuthority() {
        return name();
    }
}
