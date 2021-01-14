package com.internship.walletapi.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException implements WalletException {
    private String message;
    private HttpStatus status = NOT_FOUND;

    public ResourceNotFoundException(String message) {
        this.message = message;
    }
}
