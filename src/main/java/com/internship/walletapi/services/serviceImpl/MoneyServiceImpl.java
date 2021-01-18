package com.internship.walletapi.services.serviceImpl;

import com.internship.walletapi.exceptions.ResourceNotFoundException;
import com.internship.walletapi.mappers.TransactionToMoneyMapper;
import com.internship.walletapi.models.Money;
import com.internship.walletapi.models.Transaction;
import com.internship.walletapi.repositories.MoneyRepository;
import com.internship.walletapi.services.MoneyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.internship.walletapi.enums.TransactionType.*;
import static com.internship.walletapi.utils.ResourceHelper.saveResource;
import static com.internship.walletapi.utils.ResourceHelper.validateResourceExists;

@Slf4j
@Service
public class MoneyServiceImpl implements MoneyService {
    @Autowired
    private MoneyRepository moneyRepository;

    @Autowired
    private TransactionToMoneyMapper transactionToMoneyMapper;

    @Override
    public String withdrawOrDeposit(Transaction transaction) {
        log.info("adding or depositing: " + transaction);
        Optional<Money> optionalMoney =
                moneyRepository.getMoneyByUserIdAndCurrency(transaction.getUser().getId(), transaction.getCurrency());
        Money money = null;
        try {
            money = validateResourceExists(optionalMoney, "");
            log.info("money: " + optionalMoney.get());
            if (transaction.getType() == WITHDRAWALS) {
                if (money.getAmount() < transaction.getAmount()) return "declined";
                else  money.setAmount(money.getAmount() - transaction.getAmount());
            } else {
                money.setAmount(money.getAmount() + transaction.getAmount());
            }
        } catch (ResourceNotFoundException e) {
            log.info("" + e);
            money = transactionToMoneyMapper.map(transaction);
        }
        saveResource(money, moneyRepository);

        return "approved";
    }

    @Override
    public Money getMoneyByUserIdAndCurrency(Long userId, String currency) {
        return validateResourceExists(moneyRepository.getMoneyByUserIdAndCurrency(userId, currency), "User has no " + currency + " wallet!");
    }

    @Override
    public void saveMoney(Money money) {
        saveResource(money, moneyRepository);
    }

    @Override
    public List<Money> getMoneyByUserId(Long userId) {
        return moneyRepository.getMoneyByUser(userId);
    }
}
