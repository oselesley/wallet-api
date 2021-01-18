package com.internship.walletapi.dtos;

import com.internship.walletapi.enums.TransactionStatus;
import com.internship.walletapi.enums.TransactionType;
import com.internship.walletapi.models.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class TransactionResponseDto {
    private Long userId;

    private String currency;

    private double amount;

    private TransactionType type;

    private TransactionStatus status = TransactionStatus.PENDING;
}
