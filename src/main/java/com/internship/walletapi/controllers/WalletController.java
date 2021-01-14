package com.internship.walletapi.controllers;

import com.internship.walletapi.dtos.TransactionRequestDto;
import com.internship.walletapi.exceptions.GenericWalletException;
import com.internship.walletapi.models.Role;
import com.internship.walletapi.models.User;
import com.internship.walletapi.payload.ApiResponse;
import com.internship.walletapi.services.UserService;
import com.internship.walletapi.services.WalletService;
import com.internship.walletapi.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private UserService userService;

    @ResponseStatus(CREATED)
    @PostMapping("/deposit")
    @Parameters({
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", schema = @Schema(type = "string"))
    })
    private ResponseEntity<ApiResponse<String>> deposit (@RequestBody TransactionRequestDto trd) {
        Object sco = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) sco ;
        String username = userDetails.getUsername();
        User user = userService.fetchUser(username);

        walletService.deposit(trd, user);
        return buildResponseEntity(new ApiResponse<>("deposit transaction initiated successfully!", CREATED));
    }

    @ResponseStatus(CREATED)
    @PostMapping("/withdraw")
    @Parameters({
            @Parameter(in = ParameterIn.HEADER, name = "Authorization", schema = @Schema(type = "string"))
    })
    private ResponseEntity<ApiResponse<String>> withdraw (@RequestBody TransactionRequestDto trd)  {
        Object sco = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) sco).getUsername();
        User user = userService.fetchUser(username);
        walletService.deposit(trd, user);

        walletService.withDraw(user, trd);
        return buildResponseEntity(new ApiResponse<>("withdraw transaction completed!", CREATED));
    }
}
