package com.internship.walletapi.services.serviceImpl;

import com.internship.walletapi.dtos.BalanceCheckResponseDto;
import com.internship.walletapi.exceptions.GenericWalletException;
import com.internship.walletapi.exceptions.InsufficientFundsException;
import com.internship.walletapi.exceptions.ResourceNotFoundException;
import com.internship.walletapi.mappers.TransactionToMoneyMapper;
import com.internship.walletapi.models.Money;
import com.internship.walletapi.models.Transaction;
import com.internship.walletapi.repositories.MoneyRepository;
import com.internship.walletapi.repositories.TransactionRepository;
import com.internship.walletapi.services.MoneyService;
import com.internship.walletapi.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    @Override
    public void addTransaction(Transaction transaction) {
        saveResource(transaction, transactionRepository);
    }

    @Override
    public List<Transaction> viewPending(int pageNo, int pageSize) {
        Page<Transaction> pendingDepositsPage = transactionRepository.findAllTransactionsByStatus(PENDING.toString(), PageRequest.of(pageNo, pageSize));
        return pendingDepositsPage.getContent();
    }

    @Override
    public void approvePendingDeposit(Long pendingDepositId) {
        Optional<Transaction> optionalPendingDeposit = transactionRepository.findById(pendingDepositId);
        Transaction pendingDeposit = validateResourceExists(optionalPendingDeposit,
                "No Such Transaction!");
        if (pendingDeposit.getStatus() == APPROVED)
            throw new GenericWalletException("Transaction already approved!", BAD_REQUEST);

        verifyUserCanAccessCurrency(pendingDeposit.getUser(), pendingDeposit.getCurrency());

        String result = moneyService.addOrDeposit(pendingDeposit);
        if (result.equalsIgnoreCase("declined")) {
            pendingDeposit.setStatus(DECLINED);
            transactionRepository.save(pendingDeposit);
            throw new InsufficientFundsException("you don not have enough funds to process this request!!", NOT_ACCEPTABLE);
        } else {
            pendingDeposit.setStatus(APPROVED);
            transactionRepository.save(pendingDeposit);
        }
    }
}
