package com.internship.walletapi.controllers;

import com.internship.walletapi.dtos.TransactionRequestDto;
import com.internship.walletapi.dtos.UpdateUserRoleDto;
import com.internship.walletapi.enums.UserRole;
import com.internship.walletapi.exceptions.GenericWalletException;
import com.internship.walletapi.models.User;
import com.internship.walletapi.payload.ApiResponse;
import com.internship.walletapi.services.UserService;
import com.internship.walletapi.services.WalletService;
import com.internship.walletapi.utils.WalletHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.internship.walletapi.utils.ApiResponseBuilder.buildResponseEntity;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;


    @ResponseStatus(CREATED)
    @GetMapping("/promote/{userId}")
    @Operation(security = { @SecurityRequirement(name = "bearer-jwt") })
    private ResponseEntity<ApiResponse<String>> promoteUser (@PathVariable Long userId) {
        Object sco = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.fetchUser(((UserDetails) sco).getUsername());
        WalletHelper.validateUserAccess(user, "admin");

        log.info("in promote user role, promoting user role to: " + "elite");
        userService.updateUserRole("elite", userId);
        return buildResponseEntity(new ApiResponse<>("user role upgraded successfully!", CREATED));
    }

    @ResponseStatus(CREATED)
    @GetMapping("/demote/{userId}")
    @Operation(security = { @SecurityRequirement(name = "bearer-jwt") })
    private ResponseEntity<ApiResponse<String>> demoteUser (@PathVariable Long userId) {
        Object sco = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.fetchUser(((UserDetails) sco).getUsername());
        WalletHelper.validateUserAccess(user, "admin");

        log.info("in demote user role, demoting user role to: " + "noob");
        userService.updateUserRole("noob", userId);
        return buildResponseEntity(new ApiResponse<>("user role downgraded successfully!", CREATED));
    }

    @ResponseStatus(CREATED)
    @PostMapping("/deposit/{userId}")
    @Operation(security = { @SecurityRequirement(name = "bearer-jwt") })
    private ResponseEntity<ApiResponse<String>> adminDepositForUser (@RequestBody TransactionRequestDto trd,
                                                                 @PathVariable Long userId) {
        Object sco = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.fetchUser(((UserDetails) sco).getUsername());
        WalletHelper.validateUserAccess(user, "admin");

        User userToBeFunded = userService.fetchUser(userId);
        log.info("user to be funded: " + userToBeFunded);
        if (userToBeFunded.getUserRole() == UserRole.ADMIN)
            throw new GenericWalletException("admin cannot have wallets", METHOD_NOT_ALLOWED);

        walletService.deposit(trd, userToBeFunded, true);
        return buildResponseEntity(new ApiResponse<>("sucessfully deposited for user " + user.getUsername(), CREATED));
    }

    @ResponseStatus(CREATED)
    @PostMapping("/withdraw/{userId}")
    @Operation(security = { @SecurityRequirement(name = "bearer-jwt") })
    private ResponseEntity<ApiResponse<String>> adminWithDrawforUser (@RequestBody TransactionRequestDto trd,
                                                                      @PathVariable Long userId) {
        Object sco = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.fetchUser(((UserDetails) sco).getUsername());
        WalletHelper.validateUserAccess(user, "admin");

        User userToBeFunded = userService.fetchUser(userId);
        log.info("user to be funded: " + userToBeFunded);
        if (userToBeFunded.getUserRole() == UserRole.ADMIN)
            throw new GenericWalletException("admin cannot have wallets", METHOD_NOT_ALLOWED);

        walletService.withDraw(userToBeFunded, trd);
        return buildResponseEntity(new ApiResponse<>("sucessfully withdrew for user " + user.getUsername(), CREATED));
    }
}
