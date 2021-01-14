package com.internship.walletapi.services.serviceImpl;

import com.internship.walletapi.dtos.*;

import com.internship.walletapi.enums.TransactionStatus;
import com.internship.walletapi.enums.TransactionType;
import com.internship.walletapi.exceptions.CurrencyRequestException;
import com.internship.walletapi.exceptions.InsufficientFundsException;
import com.internship.walletapi.exceptions.InvalidCurrencyFormatException;
import com.internship.walletapi.mappers.TransactionRequestDtoToTransactionMapper;
import com.internship.walletapi.mappers.TransactionToMoneyMapper;
import com.internship.walletapi.mappers.SupportedCurrenciesMapper;
import com.internship.walletapi.models.Transaction;
import com.internship.walletapi.models.Money;
import com.internship.walletapi.models.User;
import com.internship.walletapi.repositories.TransactionRepository;
import com.internship.walletapi.repositories.MoneyRepository;
import com.internship.walletapi.services.CurrencyConverter;
import com.internship.walletapi.services.UserService;
import com.internship.walletapi.services.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.internship.walletapi.enums.TransactionStatus.*;
import static com.internship.walletapi.enums.TransactionType.*;
import static com.internship.walletapi.enums.UserRole.*;
import static com.internship.walletapi.utils.ResourceHelper.*;

import static com.internship.walletapi.utils.WalletHelper.*;
import static org.springframework.http.HttpStatus.*;


@Slf4j
@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private MoneyRepository moneyRepository;

    @Autowired
    private TransactionRepository dtr;

    @Autowired
    private UserService userService;

    @Autowired
    private CurrencyConverter currencyConverter;

    @Autowired
    private SupportedCurrenciesMapper supportedCurrenciesMapper;

    @Autowired
    private TransactionToMoneyMapper transactionToMoneyMapper;

    @Autowired
    private TransactionRequestDtoToTransactionMapper transactionMapper;

    private SupportedCurrenciesRequestDto supportedCurrenciesRequestDto;

    private String CURRENCY_CONVERSION_URL = "http://data.fixer.io/api/latest";

    public void deposit(TransactionRequestDto trd, User user) {
        validateCurrencySupported(trd.getCurrency());
        verifyUserCanAccessCurrency(user, trd.getCurrency());
        Transaction depositTransaction = mapTransactionRequestDTOtoTransaction(trd, DEPOSIT, PENDING, user);
        saveResource(depositTransaction, dtr);
    }

    @Override
    public void withDraw(Long userId, TransactionRequestDto trd)  {
        Optional<Money> optionalMoney = moneyRepository.getMoneyByUserIdAndCurrency(userId, trd.getCurrency());
        User user = userService.fetchUser(userId);
        Money money = null;
        validateCurrencySupported(trd.getCurrency());
        verifyUserCanAccessCurrency(user, trd.getCurrency());

        Transaction withdrawalTransaction = mapTransactionRequestDTOtoTransaction(trd, WITHDRAWALS, APPROVED, user);

        if (user.getUserRole().getRole() == NOOB) {
            money = validateResourceExists(optionalMoney, "user has no deposits yet!");
            deduct(money, trd.getAmount());
        } else {
            if (optionalMoney.isEmpty()) {
                String mainCurrency = user.getMainCurrency();
                optionalMoney = moneyRepository.getMoneyByUserIdAndCurrency(userId, mainCurrency);
                money = validateResourceExists(optionalMoney, "USER has no deposits!!");

                double amountToDeduct = currencyConverter.convert(trd, CURRENCY_CONVERSION_URL, mainCurrency);
                deduct(money, amountToDeduct);
            }
        }

        saveResource(withdrawalTransaction, dtr);
        moneyRepository.save(money);
    }

    private void deduct (Money money, double withdrawAmount) {
        if (withdrawAmount > money.getAmount())
            throw new InsufficientFundsException("Insufficient funds!!", NOT_ACCEPTABLE);
        money.setAmount(money.getAmount() - withdrawAmount);
    }

    @Override
    public SupportedCurrenciesResponseDto getSupportedCurrencies () {
        if (supportedCurrenciesRequestDto == null) {
            supportedCurrenciesRequestDto = getSupportedCurrencies("server error: could not get supported currencies!");
        }
        return supportedCurrenciesMapper.map(supportedCurrenciesRequestDto);
    }

    private boolean isCurrencySupported(String currency) {
        if (supportedCurrenciesRequestDto == null) {
            supportedCurrenciesRequestDto = getSupportedCurrencies("server error: could not check currency support!");
        }

        return  (supportedCurrenciesRequestDto.getSymbols().containsKey(currency));
    }

    @Override
    public void validateCurrencySupported(String currency) {
        if (!isCurrencySupported(currency))
            throw new InvalidCurrencyFormatException("currency format " +
                    currency + " is not supported!!", NOT_ACCEPTABLE);
    }

    private Transaction mapTransactionRequestDTOtoTransaction(TransactionRequestDto trd, TransactionType type, TransactionStatus status, User user) {
        Transaction transaction = transactionMapper.map(trd);
        transaction.setType(type);
        transaction.setUser(user);
        transaction.setStatus(status);

        return transaction;
    }
    private SupportedCurrenciesRequestDto getSupportedCurrencies(String errorMessage) {
            SupportedCurrenciesRequestDto supportedCurrenciesRequestDto = currencyConverter.getSupportedCurrencies(SupportedCurrenciesRequestDto.class, null);
            if (!supportedCurrenciesRequestDto.isSuccess()) {
                throw new CurrencyRequestException("could not get supported currencies!", INTERNAL_SERVER_ERROR);
            }
        return supportedCurrenciesRequestDto;
    }

    @Override
    public BalanceCheckResponseDto checkBalance() {
        return null;
    }
}
