package com.internship.walletapi.services;

import com.internship.walletapi.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void createUser(User user);

    User fetchUser(Long userId);

    User fetchUser(String username);

    Long updateUserRole(String userRole, Long UserId);
}
