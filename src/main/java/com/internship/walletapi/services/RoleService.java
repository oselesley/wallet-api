package com.internship.walletapi.services;

import com.internship.walletapi.models.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    Role fetchRole (Long id);

    Role fetchRole (String roleName);

    void createRole(Role role);
}
