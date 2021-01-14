package com.internship.walletapi.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class InsufficientFundsException extends  RuntimeException implements WalletException {
    public String message;
    public HttpStatus status;

    public InsufficientFundsException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
