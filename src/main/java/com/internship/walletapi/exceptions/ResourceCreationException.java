package com.internship.walletapi.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@Getter
@Setter
public class ResourceCreationException extends RuntimeException implements WalletException{
    private String message;
    private HttpStatus status = CONFLICT;

    public ResourceCreationException(String message) {
        this.message = message;
    }
}
