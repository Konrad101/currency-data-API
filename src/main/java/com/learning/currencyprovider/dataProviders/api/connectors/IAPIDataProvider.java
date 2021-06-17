package com.learning.currencyprovider.dataProviders.api.connectors;

import com.learning.currencyprovider.CurrencyPair;

import java.time.LocalDate;
import java.util.Set;

public interface IAPIDataProvider {
    void updateAvailableCurrencies();
    Set<String> getAvailableCurrencies();
    LocalDate getDateOfMostRecentCurrencyRate(String baseCurrency, String quoteCurrency);
    CurrencyPair getRecentCurrencyRate(String baseCurrency, String quoteCurrency);
}
