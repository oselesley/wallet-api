package com.internship.walletapi.mappers;

import com.internship.walletapi.dtos.TransactionRequestDto;
import com.internship.walletapi.models.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionRequestDtoToTransactionMapper implements Mapper<TransactionRequestDto, Transaction> {
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Transaction map(TransactionRequestDto transactionRequestDto) {
        return modelMapper.map(transactionRequestDto, Transaction.class);
    }
}
