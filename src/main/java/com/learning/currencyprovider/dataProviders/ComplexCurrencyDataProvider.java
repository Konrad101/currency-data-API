package com.learning.currencyprovider.dataProviders;

import com.learning.currencyprovider.CurrencyPair;
import com.learning.currencyprovider.dataProviders.api.APIResponse;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIConnector;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIDataProvider;
import com.learning.currencyprovider.dataProviders.api.connectors.bittrex.BittrexCurrencyProvider;
import com.learning.currencyprovider.dataProviders.api.connectors.nbp.NbpCurrencyProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
@Qualifier("ComplexProvider")
public class ComplexCurrencyDataProvider implements ICurrencyDataProvider {
    private final List<IAPIDataProvider> apiProviders;

    // bad idea
    private String lastSource;

    public ComplexCurrencyDataProvider(@Qualifier("Http") IAPIConnector connector) {
        apiProviders = new ArrayList<>(Arrays.asList(
                new BittrexCurrencyProvider(connector),
                new NbpCurrencyProvider(connector))
        );
    }

    // class that will be able to decide to from what class get data
    // when there won't be such a class, then return null

    @Override
    public void updateAvailableCurrencies() {
        apiProviders.forEach(IAPIDataProvider::updateAvailableCurrencies);
    }

    @Override
    public Set<String> getAvailableCurrencies() {
        Set<String> availableCurrencies = new HashSet<>();
        apiProviders.forEach(provider -> availableCurrencies.addAll(provider.getAvailableCurrencies()));
        return availableCurrencies;
    }

    @Override
    public APIResponse getResponse(String baseCurrency, String quoteCurrency) {
        return new CurrencyPair(baseCurrency, quoteCurrency,
                "Complex provider", new BigDecimal("1.23456"),
                LocalDate.now());
    }

}
