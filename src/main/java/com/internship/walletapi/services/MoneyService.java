package com.internship.walletapi.services;

import com.internship.walletapi.models.Money;
import com.internship.walletapi.models.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MoneyService {
    String addOrDeposit(Transaction transaction);

    Money getMoneyByUserIdAndCurrency(Long userId, String currency);

    List<Money> getMoneyByUserId(Long userId);

    void saveMoney(Money money);
}
