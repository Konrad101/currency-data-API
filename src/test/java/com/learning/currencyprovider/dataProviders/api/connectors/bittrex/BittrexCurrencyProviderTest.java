package com.learning.currencyprovider.dataProviders.api.connectors.bittrex;

import com.learning.currencyprovider.dataProviders.api.connectors.HttpRequestConnector;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIConnector;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIDataProvider;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BittrexCurrencyProviderTest {

    @Test
    void getAvailableCurrencies() {
        IAPIConnector connector = new HttpRequestConnector();
        IAPIDataProvider dataProvider = new BittrexCurrencyProvider(connector);
        Set<String> currencies = new HashSet<>(Arrays.asList(
                "USD", "BTC", "ETH", "LTC", "DOGE"
        ));

        Set<String> availableCurrencies = dataProvider.getAvailableCurrencies();
        assertNotNull(availableCurrencies);
        for (String currency : currencies) {
            assertTrue(availableCurrencies.contains(currency));
        }
    }
}