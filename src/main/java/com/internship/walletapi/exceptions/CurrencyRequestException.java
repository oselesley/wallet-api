package com.internship.walletapi.exceptions;

import com.internship.walletapi.services.WalletService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Getter
@Setter
public class CurrencyRequestException extends RuntimeException implements WalletException {
    public String message;
    public HttpStatus status;

    public CurrencyRequestException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
