package com.internship.walletapi.services.serviceImpl;

import com.internship.walletapi.models.Role;
import com.internship.walletapi.repositories.RoleRepository;
import com.internship.walletapi.services.RoleService;
import com.internship.walletapi.utils.ResourceHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.internship.walletapi.utils.ResourceHelper.*;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role fetchRole(Long id) {
        return validateResourceExists(roleRepository.findById(id), "Role Not Found!!");
    }

    @Override
    public Role fetchRole(String roleName) {
        return validateResourceExists(roleRepository.findRoleByRole(roleName),
                "No Role of type " + roleName + "exists!");
    }

    @Override
    public void createRole(Role role) {
        log.info("saving role!!");
        roleRepository.save(role);
        log.info("done saving role");
    }
}
