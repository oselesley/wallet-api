package com.internship.walletapi.rates;

import com.internship.walletapi.dtos.TransactionRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface RatesProviderService {
    <T> T getRates(TransactionRequestDto trd, String url, String currency, Class<T> t);
}
