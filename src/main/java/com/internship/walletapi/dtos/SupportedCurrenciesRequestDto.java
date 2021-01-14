package com.internship.walletapi.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class SupportedCurrenciesRequestDto {
    private boolean success;
    private Map<String, String> symbols = new HashMap<>();
    private Error error;
}


