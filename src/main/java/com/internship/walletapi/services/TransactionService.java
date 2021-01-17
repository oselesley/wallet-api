package com.internship.walletapi.services;

import com.internship.walletapi.dtos.TransactionResponseDto;
import com.internship.walletapi.models.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {
    void addTransaction(Transaction transaction);

    List<TransactionResponseDto> viewPending (int pageNo, int pageSize);

    List<TransactionResponseDto> viewAll (int pageNo, int pageSize);

    void approvePendingDeposit(Long pendingDepositId);
}
