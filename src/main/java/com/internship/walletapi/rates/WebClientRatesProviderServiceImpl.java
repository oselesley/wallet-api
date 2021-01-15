package com.internship.walletapi.rates;

import com.internship.walletapi.dtos.TransactionRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Primary
@Service
public class WebClientRatesProviderServiceImpl implements RatesProviderService {
    @Autowired
    private Environment env;
    @Override
    public <T> T getRates(String transactionCurrency, String url, String currency, Class<T> t) {
        WebClient webClient = WebClient.create(url);
        T s = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("access_key", env.getProperty("CURRENCY_CONVERTER_API_KEY"))
                        .queryParam("base", "EUR")
                        .queryParam("symbols", currency + ", " + transactionCurrency)
                        .build())
                .retrieve()
                .bodyToFlux(t).blockFirst();
        log.info(s + "");
        return s;
    }
}
