package com.internship.walletapi.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BalanceCheckResponseDto {
    private String message;
    private double totalBalance;
    private String mainCurrency;
    private List<MoneyResponseDto> monies = new ArrayList<>();

    public BalanceCheckResponseDto withMessage(String message) {
        this.message = message;
        return this;
    }
    public BalanceCheckResponseDto withTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
        return this;
    }
    public BalanceCheckResponseDto withMainCurrency(String mainCurrency) {
        this.mainCurrency = mainCurrency;
        return this;
    }

    public BalanceCheckResponseDto withMonies(List<MoneyResponseDto> monies) {
        this.monies = monies;
        return this;
    }
}
