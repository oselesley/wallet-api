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
    private List<MoneyResponseDto> moneyResponseDtoList = new ArrayList<>();
}
