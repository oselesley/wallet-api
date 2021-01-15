package com.internship.walletapi.services.serviceImpl;

import com.internship.walletapi.dtos.*;

import com.internship.walletapi.enums.TransactionStatus;
import com.internship.walletapi.enums.TransactionType;
import com.internship.walletapi.exceptions.CurrencyRequestException;
import com.internship.walletapi.exceptions.InsufficientFundsException;
import com.internship.walletapi.exceptions.InvalidCurrencyFormatException;
import com.internship.walletapi.exceptions.ResourceNotFoundException;
import com.internship.walletapi.mappers.TransactionRequestDtoToTransactionMapper;
import com.internship.walletapi.mappers.TransactionToMoneyMapper;
import com.internship.walletapi.mappers.SupportedCurrenciesMapper;
import com.internship.walletapi.models.Transaction;
import com.internship.walletapi.models.Money;
import com.internship.walletapi.models.User;
import com.internship.walletapi.repositories.TransactionRepository;
import com.internship.walletapi.repositories.MoneyRepository;
import com.internship.walletapi.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
    private UserService userService;

    @Autowired
    private CurrencyConverter currencyConverter;

    @Autowired
    private SupportedCurrenciesMapper supportedCurrenciesMapper;

    @Autowired
    private TransactionToMoneyMapper transactionToMoneyMapper;

    @Autowired
    private TransactionRequestDtoToTransactionMapper transactionMapper;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private MoneyService moneyService;


    private SupportedCurrenciesRequestDto supportedCurrenciesRequestDto;

    private String CURRENCY_CONVERSION_URL = "http://data.fixer.io/api/latest";

    @Transactional
    public void deposit(TransactionRequestDto trd, User user, boolean admin) {
        validateCurrencySupported(trd.getCurrency());
        verifyUserCanAccessCurrency(user, trd.getCurrency());
        Transaction depositTransaction = mapTransactionRequestDTOtoTransaction(trd, DEPOSIT, PENDING, user);
        transactionService.addTransaction(depositTransaction);
        if (admin) {
            moneyService.addOrDeposit(depositTransaction);
        }
    }

    @Override
    public void withDraw(User user, TransactionRequestDto trd)  {
        Money money = null;
        String mainCurrency = user.getMainCurrency();
        validateCurrencySupported(trd.getCurrency());
        verifyUserCanAccessCurrency(user, trd.getCurrency());

        Transaction withdrawalTransaction = mapTransactionRequestDTOtoTransaction(trd, WITHDRAWALS, APPROVED, user);

        if (user.getUserRole().getRole() == NOOB) {
            money = moneyService.getMoneyByUserIdAndCurrency(user.getId(), user.getMainCurrency());
            deduct(money, trd.getAmount());
        } else {
                try {
                    money = moneyService.getMoneyByUserIdAndCurrency(user.getId(), trd.getCurrency());
                } catch (ResourceNotFoundException e) {
                    money = moneyService.getMoneyByUserIdAndCurrency(user.getId(), mainCurrency);
                }

            double amountToDeduct = currencyConverter.convert(trd.getCurrency(), trd.getAmount(), CURRENCY_CONVERSION_URL, mainCurrency);
            deduct(money, amountToDeduct);
        }

        transactionService.addTransaction(withdrawalTransaction);
        moneyService.saveMoney(money);
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

    // TODO: Refactor currency conversion logic for performance
    @Override
    public BalanceCheckResponseDto checkBalance(Long userId) {
        BalanceCheckResponseDto balanceCheckResponseDto = new BalanceCheckResponseDto();
        AtomicReference<Double> total = new AtomicReference<>(0.0);

        User user = userService.fetchUser(userId);
        List<Money> monies =  moneyService.getMoneyByUserId(userId);
        List<MoneyResponseDto> moneyResponseDtos = monies.stream().map(money -> {
            total.updateAndGet(v -> (v + currencyConverter.convert(money.getCurrency(), money.getAmount(), CURRENCY_CONVERSION_URL, user.getMainCurrency())));
            return new MoneyResponseDto().withAmount(money.getAmount()).withCurrency(money.getCurrency());
        }).collect(Collectors.toList());

        return new BalanceCheckResponseDto().withMessage("you have a total of " +
                user.getMainCurrency() + total + "in your wallet")
                .withMainCurrency(user.getMainCurrency())
                .withTotalBalance(total.get())
                .withMonies(moneyResponseDtos);

    }
}
