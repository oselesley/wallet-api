package com.internship.walletapi.mappers;

import com.internship.walletapi.enums.TransactionStatus;
import com.internship.walletapi.enums.TransactionType;
import com.internship.walletapi.models.User;

import javax.transaction.Transaction;

public interface Mapper<From, To> {
    To map (From from);
}
