package com.internship.walletapi.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CurrencyNotAuthorizedException extends RuntimeException implements WalletException {
        public String message;
        public HttpStatus status;

        public CurrencyNotAuthorizedException(String message, HttpStatus status) {
            this.message = message;
            this.status = status;
        }
}
