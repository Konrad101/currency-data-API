package com.learning.currencyprovider.dataProviders.api.connectors.nbp;

import com.learning.currencyprovider.dataProviders.api.connectors.HttpRequestConnector;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIConnector;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIDataProvider;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NbpCurrencyProviderTest {

    @Test
    void getAvailableCurrencies() {
        IAPIConnector connector = new HttpRequestConnector();
        IAPIDataProvider dataProvider = new NbpCurrencyProvider(connector);

        Set<String> currencies = new HashSet<>(Arrays.asList(
                "USD", "AUD", "PLN", "CAD"
        ));

        Set<String> availableCurrencies = dataProvider.getAvailableCurrencies();
        assertNotNull(availableCurrencies);
        for(String currency : currencies){
            assertTrue(availableCurrencies.contains(currency));
        }
    }
}