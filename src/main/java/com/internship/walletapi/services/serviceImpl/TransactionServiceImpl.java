package com.internship.walletapi.services.serviceImpl;

import com.internship.walletapi.dtos.TransactionResponseDto;
import com.internship.walletapi.exceptions.GenericWalletException;
import com.internship.walletapi.exceptions.InsufficientFundsException;
import com.internship.walletapi.mappers.TransactionToTransactionResponseDtoMapper;
import com.internship.walletapi.models.Transaction;
import com.internship.walletapi.repositories.TransactionRepository;
import com.internship.walletapi.services.MoneyService;
import com.internship.walletapi.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.internship.walletapi.enums.TransactionStatus.*;
import static com.internship.walletapi.utils.ResourceHelper.*;
import static com.internship.walletapi.utils.WalletHelper.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MoneyService moneyService;

    @Autowired
    private TransactionToTransactionResponseDtoMapper transactionToTransactionResponseDtoMapper;

    @Override
    public void addTransaction(Transaction transaction) {
        saveResource(transaction, transactionRepository);
    }

    @Override
    public List<TransactionResponseDto> viewPending(int pageNo, int pageSize) {
        Page<Transaction> pendingDepositsPage = transactionRepository.findAllTransactionsByStatus(PENDING, PageRequest.of(pageNo, pageSize));
        List<TransactionResponseDto> responseDtos = new ArrayList<>();
        pendingDepositsPage.getContent().forEach(transaction -> {
            log.info("mapping transaction: " + transaction);
            responseDtos.add(transactionToTransactionResponseDtoMapper.map(transaction));
        });
        return responseDtos;
    }

    @Override
    public List<TransactionResponseDto> viewAll(int pageNo, int pageSize) {
        log.info("fetching transactions");
        Page<Transaction> pendingDepositsPage = transactionRepository.findAll(PageRequest.of(pageNo, pageSize));
        log.info("" + pendingDepositsPage.toList());
        List<TransactionResponseDto> responseDtos = new ArrayList<>();
        pendingDepositsPage.getContent().forEach(transaction -> {
            log.info("mapping transaction: " + transaction);
            responseDtos.add(transactionToTransactionResponseDtoMapper.map(transaction));
        });
        return responseDtos;
    }

    @Override
    public void approvePendingDeposit(Long pendingDepositId) {
        log.info("approving!!");
        Optional<Transaction> optionalPendingDeposit = transactionRepository.findById(pendingDepositId);

        Transaction pendingDeposit = validateResourceExists(optionalPendingDeposit,
                "No Such Transaction!");
        if (pendingDeposit.getStatus() == APPROVED)
            throw new GenericWalletException("Transaction already approved!", BAD_REQUEST);

        verifyUserCanAccessCurrency(pendingDeposit.getUser(), pendingDeposit.getCurrency());

        String result = moneyService.withdrawOrDeposit(pendingDeposit);
        if (result.equalsIgnoreCase("declined")) {
            pendingDeposit.setStatus(DECLINED);
            transactionRepository.save(pendingDeposit);
            throw new InsufficientFundsException("you do not have enough funds to process this request!!", NOT_ACCEPTABLE);
        } else {
            pendingDeposit.setStatus(APPROVED);
            transactionRepository.save(pendingDeposit);
        }
    }
}
