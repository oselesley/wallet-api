package com.internship.walletapi.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoneyResponseDto {
    private String currency;
    private double amount;

    public MoneyResponseDto withCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public MoneyResponseDto withAmount(double amount) {
        this.amount = amount;
        return this;
    }
}
