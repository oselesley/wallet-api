package com.internship.walletapi.exceptions;

import org.springframework.http.HttpStatus;

public interface WalletException {
    String getMessage ();

    HttpStatus getStatus();
}
