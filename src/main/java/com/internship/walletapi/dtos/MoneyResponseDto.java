package com.internship.walletapi.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoneyResponseDto {
    private String currency;
    private String amount;
}
