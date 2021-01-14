package com.internship.walletapi.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SupportedCurrenciesResponseDto {
    private String message;
    private Map<String, String> currencies;
}
