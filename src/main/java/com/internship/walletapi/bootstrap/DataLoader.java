package com.internship.walletapi.bootstrap;

import com.internship.walletapi.models.Role;
import com.internship.walletapi.models.User;
import com.internship.walletapi.repositories.RoleRepository;
import com.internship.walletapi.services.RoleService;
import com.internship.walletapi.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.internship.walletapi.enums.UserRole.*;

@Slf4j
@Component
public class DataLoader {
    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct @Order(1)
    private void init () {
        log.info("API KEY: " + env.getProperty("CURRENCY_CONVERTER_API_KEY"));
       try {
           Role role = new Role();
           role.setName(ADMIN.name());
           role.setRole(ADMIN);

           Role role2 = new Role();
           role2.setName(ELITE.name());
           role2.setRole(ELITE);

           Role role3 = new Role();
           role3.setName(NOOB.name());
           role3.setRole(NOOB);

           roleService.createRole(role);
           roleService.createRole(role2);
           roleService.createRole(role3);

           User user2 = new User();
           user2.setLastName("osereme");
           user2.setFirstName("okoduwa");
           user2.setPassword(passwordEncoder.encode("oseremepass"));
           user2.setMainCurrency("NGN");
           user2.setEmail("lesley@gmail.com");
           user2.setUsername("lesliej");
           user2.setUserRole(role);
           userService.createUser(user2);
       } catch (Exception e) {
           log.info("Data already exists");
       }
    }
}
