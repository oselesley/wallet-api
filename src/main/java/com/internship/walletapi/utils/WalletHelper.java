package com.internship.walletapi.utils;

import com.internship.walletapi.enums.UserRole;
import com.internship.walletapi.exceptions.CurrencyNotAuthorizedException;
import com.internship.walletapi.exceptions.GenericWalletException;
import com.internship.walletapi.models.Role;
import com.internship.walletapi.models.User;
import com.internship.walletapi.services.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.MethodNotAllowedException;

import static com.internship.walletapi.enums.UserRole.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
public class WalletHelper {
    public static void verifyUserCanAccessCurrency (User user, String currency) {
        if (user.getUserRole().getRole() == NOOB && !user.getMainCurrency().equalsIgnoreCase(currency))
            throw new CurrencyNotAuthorizedException("NOOB user cannot access currency", NOT_ACCEPTABLE);
    }

    public static void setUserRole (String role, RoleService roleService, User user) {
        log.info("in set user role!!");
        switch (role.toLowerCase()) {
            case "noob":
                Role userRole = roleService.fetchRole(3L);
                user.setUserRole(userRole);
                break;
            case "elite":
                userRole = roleService.fetchRole(2L);
                user.setUserRole(userRole);
                break;
            case "admin":
                userRole = roleService.fetchRole(1L);
                user.setUserRole(userRole);
                break;
            default:
                throw new GenericWalletException(role + " is not a valid role!!", BAD_REQUEST);
        }
    }

    public static void validateUserAccess(User user, String... roles){
        for (int i = 0; i < roles.length; i++) {
            if(roles[i].equalsIgnoreCase(user.getUserRole().getName())) return;
        }

        throw new GenericWalletException("role " + user.getUserRole().getName() + " is unathorized for this route", METHOD_NOT_ALLOWED);
    }
}
