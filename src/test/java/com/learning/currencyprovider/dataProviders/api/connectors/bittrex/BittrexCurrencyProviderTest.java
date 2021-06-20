package com.learning.currencyprovider.dataProviders.api.connectors.bittrex;

import com.learning.currencyprovider.dataProviders.api.connectors.HttpRequestConnector;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIConnector;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIDataProvider;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BittrexCurrencyProviderTest {
    private final static IAPIConnector connector = new HttpRequestConnector();
    private final static IAPIDataProvider dataProvider = new BittrexCurrencyProvider(connector);

    @Test
    void getAvailableCurrencies() {
        Set<String> currencies = new HashSet<>(Arrays.asList(
                "USD", "BTC", "ETH", "LTC", "DOGE"
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
                "USD", "EUR", "BTC", "LTC", "DOGE"
        ));

        List<String> unavailableCurrencies = new ArrayList<>(Arrays.asList(
                "PLN", "CAD", "AUD", "", null
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