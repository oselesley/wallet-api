package com.internship.walletapi.services.serviceImpl;

import com.internship.walletapi.dtos.AuthResponseDto;
import com.internship.walletapi.dtos.SignupRequestDto;
import com.internship.walletapi.enums.UserRole;
import com.internship.walletapi.mappers.UserMapper;
import com.internship.walletapi.models.Role;
import com.internship.walletapi.models.User;
import com.internship.walletapi.repositories.UserRepository;
import com.internship.walletapi.services.AuthService;
import com.internship.walletapi.services.RoleService;
import com.internship.walletapi.services.UserService;
import com.internship.walletapi.utils.ResourceHelper;
import com.internship.walletapi.utils.WalletHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.internship.walletapi.utils.WalletHelper.*;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Override
    public void signup(SignupRequestDto dto) {
        log.info("in authservice sign up" + dto + "");
        User user = userMapper.map(dto);
        setUserRole(dto.getRole(), roleService, user);
        userService.createUser(user);
    }

    @Override
    public AuthResponseDto signin() {
        return null;
    }
}
