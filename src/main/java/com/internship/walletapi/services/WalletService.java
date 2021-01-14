package com.internship.walletapi.services;

import com.internship.walletapi.dtos.BalanceCheckResponseDto;
import com.internship.walletapi.dtos.SupportedCurrenciesResponseDto;
import com.internship.walletapi.dtos.TransactionRequestDto;
import com.internship.walletapi.models.User;
import org.springframework.stereotype.Service;

@Service
public interface WalletService {
    void deposit(TransactionRequestDto trd, User user);

    void withDraw(User user, TransactionRequestDto trd);

    SupportedCurrenciesResponseDto getSupportedCurrencies ();

    void validateCurrencySupported(String currency);

    BalanceCheckResponseDto checkBalance ();
}
