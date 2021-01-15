package com.internship.walletapi.controllers;

import com.internship.walletapi.dtos.TransactionRequestDto;
import com.internship.walletapi.models.Transaction;
import com.internship.walletapi.models.User;
import com.internship.walletapi.payload.ApiResponse;
import com.internship.walletapi.services.TransactionService;
import com.internship.walletapi.services.UserService;
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
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.internship.walletapi.utils.ApiResponseBuilder.*;
import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;


    @ResponseStatus(CREATED)
    @GetMapping("/approve/{transactionId}")
    @Operation(security = { @SecurityRequirement(name = "bearer-jwt") })
    private ResponseEntity<ApiResponse<String>> approve (@PathVariable Long transactionId) {
        Object sco = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.fetchUser(((UserDetails) sco).getUsername());
        WalletHelper.validateUserAccess(user, "admin");
        transactionService.approvePendingDeposit(transactionId);
        return buildResponseEntity(new ApiResponse<>("deposit transaction approved!", CREATED));
    }

    @ResponseStatus(CREATED)
    @GetMapping("/getAll/{pageNo}/{pageLength}")
    @Operation(security = { @SecurityRequirement(name = "bearer-jwt") })
    private ResponseEntity<ApiResponse<List<Transaction>>> fetchAllTransactions (
                                                      @PathVariable int pageNo,
                                                      @PathVariable int pageLength) {
        Object sco = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.fetchUser(((UserDetails) sco).getUsername());
        WalletHelper.validateUserAccess(user, "admin");

        log.info("fetch all pending requests from page number: " + pageNo + " , requests per page: " + pageLength);
        List<Transaction> transactionList = transactionService.viewPending(pageNo, pageLength);
        return buildResponseEntity(new ApiResponse<>("succeeded!!", CREATED, transactionList));
    }
}
