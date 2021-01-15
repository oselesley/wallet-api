package com.internship.walletapi.services.serviceImpl;

import com.internship.walletapi.dtos.MoneyConverterResponseDto;
import com.internship.walletapi.dtos.TransactionRequestDto;
import com.internship.walletapi.exceptions.CurrencyRequestException;
import com.internship.walletapi.rates.RatesProviderService;
import com.internship.walletapi.services.CurrencyConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Primary
@Service
public class CurrencyConverterImpl implements CurrencyConverter {
    @Autowired
    private Environment env;

    @Autowired
    private RatesProviderService ratesProviderService;

    private String SUPPORTED_CURRENCY_URL = "http://data.fixer.io/api/symbols";

    @Override
    public <T> T getSupportedCurrencies(Class<T> t, @Nullable String url) {
        WebClient webClient = WebClient.create(url == null ? SUPPORTED_CURRENCY_URL : url );
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("access_key", env.getProperty("CURRENCY_CONVERTER_API_KEY"))
                        .build())
                .retrieve()
                .bodyToFlux(t).blockFirst();
    }

    @Override
    public double convert(String transactionCurrency, double amount, String url, String currency) {
        MoneyConverterResponseDto moneyConverterResponseDto = ratesProviderService.getRates(transactionCurrency, url, currency, MoneyConverterResponseDto.class);

        if (Objects.isNull(moneyConverterResponseDto) ||
                !moneyConverterResponseDto.isSuccess() ||
                moneyConverterResponseDto.getRates().values().size() < 2)
            throw new CurrencyRequestException("Could perform transaction", INTERNAL_SERVER_ERROR);

        List<Double> totalRates = moneyConverterResponseDto
                .getRates()
                .values()
                .stream()
                .map(Double::valueOf).collect(Collectors.toList());
        log.info(totalRates + " " + moneyConverterResponseDto.getRates().values());
        return amount *  ( totalRates.get(0)/ totalRates.get(1));
    }

    private <T> T getRates(TransactionRequestDto trd, String url, String currency, Class<T> t) {
        WebClient webClient = WebClient.create(url);
        T s = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("access_key", env.getProperty("CURRENCY_CONVERTER_API_KEY"))
                        .queryParam("base", "EUR")
                        .queryParam("symbols", currency, trd.getCurrency())
                        .build())
                .retrieve()
                .bodyToFlux(t).blockFirst();
        log.info(s + "");
        return s;
    }
}
