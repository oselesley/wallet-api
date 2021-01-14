package com.internship.walletapi.services.serviceImpl;

import com.internship.walletapi.models.User;
import com.internship.walletapi.repositories.RoleRepository;
import com.internship.walletapi.repositories.UserRepository;
import com.internship.walletapi.services.RoleService;
import com.internship.walletapi.services.UserService;
import com.internship.walletapi.services.WalletService;
import com.internship.walletapi.utils.WalletHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.internship.walletapi.utils.ResourceHelper.*;
import static com.internship.walletapi.utils.WalletHelper.*;

@Slf4j
@Primary
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private RoleService roleService;

    @Override
    public User fetchUser(String username) {
        return validateResourceExists(userRepository.findUserByUsernameOrEmail(username), "User Not Found!!");
    }

    @Override
    public void createUser(User user) {
        log.info("in create user");
        walletService.validateCurrencySupported(user.getMainCurrency());
        log.info("in after validating currency");
        userRepository.save(user);
    }

    @Override
    public User fetchUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return validateResourceExists(user, "User Not Found");
    }

    @Override
    public Long updateUserRole(String userRole, Long userId) {
        User fetchedUser =  fetchUser(userId);
        setUserRole(userRole, roleService, fetchedUser);
        return saveResource(fetchedUser, userRepository);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsernameOrEmail(email);
        if (user.isPresent()) {
            log.info(user.get() + "");
            user.get().setAccountNonLocked(true);
        }
        return validateResourceExists(user, "User Not Found!!");
    }
}
