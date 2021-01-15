package com.internship.walletapi.controllers;


import com.internship.walletapi.exceptions.*;
import com.internship.walletapi.payload.ApiResponse;
import com.internship.walletapi.utils.ApiResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import static com.internship.walletapi.utils.ApiResponseBuilder.*;
import static com.internship.walletapi.utils.ApiResponseBuilder.buildResponseEntity;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class RequestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ce) {
        ApiResponse<String> ar = new ApiResponse<>(ce.getMessage(), INTERNAL_SERVER_ERROR);
        return buildErrorResponseEntity(ar);
    }
    @ExceptionHandler({ResourceNotFoundException.class,
                CurrencyRequestException.class,
                InsufficientFundsException.class,
                ResourceCreationException.class,
                CurrencyNotAuthorizedException.class,
                InvalidCurrencyFormatException.class,
                GenericWalletException.class})
    public ResponseEntity<Object> handleCustomException(WalletException ce) {
        ApiResponse<String> ar = new ApiResponse<>(ce.getMessage(), ce.getStatus());
        return buildErrorResponseEntity(ar);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiResponse<Object> ar = new ApiResponse<>(HttpStatus.BAD_REQUEST);
        ar.addValidationError(ex.getBindingResult().getAllErrors());
        ar.setError("Validation Error");
        return buildErrorResponseEntity(ar);
    }
}

