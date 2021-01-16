package com.internship.walletapi.controllers;

import com.internship.walletapi.dtos.BalanceCheckResponseDto;
import com.internship.walletapi.dtos.TransactionRequestDto;
import com.internship.walletapi.exceptions.GenericWalletException;
import com.internship.walletapi.models.Role;
import com.internship.walletapi.models.User;
import com.internship.walletapi.payload.ApiResponse;
import com.internship.walletapi.services.UserService;
import com.internship.walletapi.services.WalletService;
import com.internship.walletapi.utils.ApiResponseBuilder;
import com.internship.walletapi.utils.WalletHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.internship.walletapi.enums.UserRole.*;
import static com.internship.walletapi.utils.ApiResponseBuilder.*;
import static org.springframework.http.HttpStatus.*;


// "%s %s has been successfully deposited", trd.getAmount(), trd.getCurrency()
@Slf4j
@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @ResponseStatus(CREATED)
    @PostMapping("/deposit")
    @Operation(security = { @SecurityRequirement(name = "bearer-jwt") })
    public ResponseEntity<ApiResponse<String>> deposit (@RequestBody TransactionRequestDto trd) {
        Object sco = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.fetchUser(((UserDetails) sco).getUsername());
        WalletHelper.validateUserAccess(user, "elite", "noob");

        walletService.deposit(trd, user, false);
        return buildResponseEntity(new ApiResponse<>("deposit transaction initiated successfully!", CREATED));
    }

    @Autowired
    private UserService userService;

    @ResponseStatus(CREATED)
    @PostMapping("/fund-wallet")
    @Operation(security = { @SecurityRequirement(name = "bearer-jwt") })
    public ResponseEntity<ApiResponse<String>> withdraw (@RequestBody TransactionRequestDto trd)  {
        Object sco = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.fetchUser(((UserDetails) sco).getUsername());
        WalletHelper.validateUserAccess(user, "elite", "noob");

        walletService.withDraw(user, trd);
        return buildResponseEntity(new ApiResponse<>("withdraw transaction completed!", CREATED));
    }

    @ResponseStatus(CREATED)
    @GetMapping("/balance")
    @Operation(security = { @SecurityRequirement(name = "bearer-jwt") })
    public ResponseEntity<ApiResponse<BalanceCheckResponseDto>> checkBalance ()  {
        Object sco = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.fetchUser(((UserDetails) sco).getUsername());
        WalletHelper.validateUserAccess(user, "elite", "noob");

        BalanceCheckResponseDto balance = walletService.checkBalance( user.getId());
        return buildResponseEntity(new ApiResponse<>("withdraw transaction completed!", CREATED, balance));
    }
}
