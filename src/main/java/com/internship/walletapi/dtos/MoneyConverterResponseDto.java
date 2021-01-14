package com.internship.walletapi.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Map;


/**
 * {
 *     "success": true,
 *     "timestamp": 1610526427,
 *     "base": "USD",
 *     "date": "2021-01-13",
 *     "rates": {
 *         "NGN": 381.198534
 *     }
 * }
 */
@Getter
@Setter
@ToString
public class MoneyConverterResponseDto {
    private boolean success;
    private long timestamp;
    private  LocalDate date;
    private Map<String,String> rates;
    private Error error;
}

