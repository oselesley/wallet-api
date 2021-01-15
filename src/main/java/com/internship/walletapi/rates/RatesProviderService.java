package com.internship.walletapi.rates;

import com.internship.walletapi.dtos.TransactionRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface RatesProviderService {
    <T> T getRates(String transactionCurrency, String url, String currency, Class<T> t);
}
