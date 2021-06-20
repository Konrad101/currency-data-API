package com.learning.currencyprovider.dataProviders.api.connectors.nbp;

import com.learning.currencyprovider.dataProviders.api.connectors.HttpRequestConnector;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIConnector;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIDataProvider;
import com.learning.currencyprovider.dataProviders.api.connectors.bittrex.BittrexCurrencyProvider;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

class NbpCurrencyProviderTest {
    private final static IAPIConnector connector = new HttpRequestConnector();
    private final static IAPIDataProvider dataProvider = new NbpCurrencyProvider(connector);

    @Test
    void getAvailableCurrencies() {
        Set<String> currencies = new HashSet<>(Arrays.asList(
                "USD", "AUD", "PLN", "CAD"
        ));

        Set<String> availableCurrencies = dataProvider.getAvailableCurrencies();
        assertNotNull(availableCurrencies);
        for (String currency : currencies) {
            assertTrue(availableCurrencies.contains(currency));
        }
    }

    @Test
    void getRecentCurrencyRate() {
        List<String> availableCurrencies = new ArrayList<>(Arrays.asList(
                "USD", "EUR", "AUD", "CAD", "PLN"
        ));

        List<String> unavailableCurrencies = new ArrayList<>(Arrays.asList(
                "BTC", "ETH", "\n\n\n\t\t\t", "", null
        ));

        for (String baseCurrency : availableCurrencies) {
            for (String quoteCurrency : availableCurrencies) {
                if (!baseCurrency.equals(quoteCurrency)) {
                    assertNotNull(dataProvider.getRecentCurrencyRate(baseCurrency, quoteCurrency));
                }
            }
        }

        for (String baseCurrency : unavailableCurrencies) {
            for (String quoteCurrency : unavailableCurrencies) {
                assertNull(dataProvider.getRecentCurrencyRate(baseCurrency, quoteCurrency));
            }

            for (String quoteCurrency : unavailableCurrencies) {
                assertNull(dataProvider.getRecentCurrencyRate(baseCurrency, quoteCurrency));
            }
        }
    }
}