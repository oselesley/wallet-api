package com.internship.walletapi.enums;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

import static com.internship.walletapi.enums.Permission.*;

public enum UserRole {
    ADMIN(Sets.newHashSet(NOOB_READ, NOOB_WRITE, ELITE_READ, ELITE_WRITE, ADMIN_WRITE, ADMIN_READ)),
    ELITE(Sets.newHashSet(NOOB_READ, NOOB_WRITE, ELITE_READ, ELITE_WRITE)),
    NOOB(Sets.newHashSet(NOOB_READ, NOOB_WRITE));

    private final Set<Permission> permissions;
    UserRole(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> authorities  = new HashSet<>();
        permissions.forEach(permission -> {
            authorities.add(new SimpleGrantedAuthority(permission.getPermission()));
        });
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
