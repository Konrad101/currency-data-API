package com.learning.currencyprovider.dataProviders;

import com.learning.currencyprovider.dataProviders.api.APIResponse;

import java.util.List;

public interface ICurrencyDataProvider {
    List<String> getAvailableCurrencies();
    APIResponse getResponse(String baseCurrency, String quoteCurrency);
    String getSource();
}
