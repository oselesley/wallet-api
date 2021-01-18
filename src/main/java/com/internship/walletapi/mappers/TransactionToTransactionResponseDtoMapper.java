package com.internship.walletapi.mappers;

import com.internship.walletapi.dtos.TransactionResponseDto;
import com.internship.walletapi.models.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransactionToTransactionResponseDtoMapper implements Mapper<Transaction, TransactionResponseDto> {
    @Autowired
    private ModelMapper mapper;

    @Override
    public TransactionResponseDto map(Transaction transaction) {
        TransactionResponseDto trd = mapper.map(transaction, TransactionResponseDto.class);

        trd.setUserId(transaction.getUser().getId());

        log.info("inside mapper, mapping trd..." + trd);
        return trd;
    }
}
