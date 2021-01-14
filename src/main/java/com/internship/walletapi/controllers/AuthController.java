package com.internship.walletapi.controllers;

import com.internship.walletapi.dtos.AuthResponseDto;
import com.internship.walletapi.dtos.LoginDto;
import com.internship.walletapi.dtos.SignupRequestDto;
import com.internship.walletapi.payload.ApiResponse;
import com.internship.walletapi.services.AuthService;
import com.internship.walletapi.services.UserService;
import com.internship.walletapi.utils.ApiResponseBuilder;
import com.internship.walletapi.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.internship.walletapi.utils.ApiResponseBuilder.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @ResponseStatus(OK)
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup (@Valid @RequestBody SignupRequestDto dto) {
        log.info("in signup controller");
        authService.signup(dto);
        return buildResponseEntity(new ApiResponse<>("user signed up sucessfully", OK));
    }

    @ResponseStatus(OK)
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login (@RequestBody LoginDto dto) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsernameOrEmail(), dto.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("incorrect username or passoword!");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUsernameOrEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        return buildResponseEntity(new ApiResponse<>("user signed in sucessfully", OK, new AuthResponseDto(jwt)));
    }

    @ResponseStatus(CREATED)
    @GetMapping("/upgrade/{roleType}/{userId}")
    private ResponseEntity<ApiResponse<String>> upgradeUserRole (
            @PathVariable String roleType,
            @PathVariable Long userId) {
        userService.updateUserRole(roleType, userId);
        return buildResponseEntity(new ApiResponse<>("user role upgraded successfully!", CREATED));
    }
}
