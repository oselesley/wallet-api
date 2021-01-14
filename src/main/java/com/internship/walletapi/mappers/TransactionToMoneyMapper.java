package com.internship.walletapi.mappers;

import com.internship.walletapi.models.Money;
import com.internship.walletapi.models.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionToMoneyMapper implements Mapper<Transaction, Money> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Money map(Transaction transaction) {
        return modelMapper.map(transaction, Money.class);
    }
}
