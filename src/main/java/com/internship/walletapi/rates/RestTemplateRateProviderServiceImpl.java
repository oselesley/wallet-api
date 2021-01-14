package com.internship.walletapi.rates;

import com.internship.walletapi.dtos.TransactionRequestDto;
import com.internship.walletapi.exceptions.CurrencyRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Service
public class RestTemplateRateProviderServiceImpl implements RatesProviderService {
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private Environment env;

    @Override
    public <T> T getRates(TransactionRequestDto trd, String url, String currency, Class<T> t) {
        ResponseEntity<T> response = restTemplate.exchange(url, PUT, EMPTY, t, Map.of(
                "access_key", env.getProperty("CURRENCY_CONVERTER_API_KEY"),
                "from", trd.getCurrency(),
                "to", currency,
                "amount", trd.getAmount()));

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null)
            throw new CurrencyRequestException("Request could not be completed!", INTERNAL_SERVER_ERROR);
        return response.getBody();
    }
}
