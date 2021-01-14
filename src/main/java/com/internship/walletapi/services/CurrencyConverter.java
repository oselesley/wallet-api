package com.internship.walletapi.services;

import com.internship.walletapi.dtos.TransactionRequestDto;
import org.springframework.lang.Nullable;

public interface CurrencyConverter {
    <T> T getSupportedCurrencies(Class<T> t, @Nullable String url);

    double convert(TransactionRequestDto trd, String url, String currency);
}
