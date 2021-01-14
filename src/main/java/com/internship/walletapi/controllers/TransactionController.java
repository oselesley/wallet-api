package com.internship.walletapi.controllers;

import com.internship.walletapi.dtos.TransactionRequestDto;
import com.internship.walletapi.payload.ApiResponse;
import com.internship.walletapi.services.TransactionService;
import com.internship.walletapi.utils.ApiResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.lang.reflect.Parameter;

import static com.internship.walletapi.utils.ApiResponseBuilder.*;
import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;


    @ResponseStatus(CREATED)
    @GetMapping("/approve/{transactionId}")
    private ResponseEntity<ApiResponse<String>> approve (@Valid @PathVariable Long transactionId) {
        transactionService.approvePendingDeposit(transactionId);
        return buildResponseEntity(new ApiResponse<>("deposit transaction approved!", CREATED));
    }

    @ResponseStatus(CREATED)
    @GetMapping("/getAll/{pageNo}/{pageLength}")
    private ResponseEntity<ApiResponse<String>> fetchAllTransactions (@Valid @RequestBody TransactionRequestDto trd,
                                                      BindingResult br,
                                                      @PathVariable int pageNo,
                                                      @PathVariable int pageLength) {
        log.info("fetch all pending requests from page number: " + pageNo + " , requests per page: " + pageLength);
        transactionService.viewPending(pageNo, pageLength);
        return buildResponseEntity(new ApiResponse<>("deposit transaction initiated successfully!", CREATED));
    }

}
