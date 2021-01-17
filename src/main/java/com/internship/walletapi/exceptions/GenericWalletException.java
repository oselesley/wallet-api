package com.internship.walletapi.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class GenericWalletException extends RuntimeException implements WalletException {
    private String message;
    private HttpStatus status;

    public GenericWalletException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
