package com.internship.walletapi.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransactionRequestDto {
    private String currency;
    private double amount;
}
