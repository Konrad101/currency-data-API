package com.learning.currencyprovider.dataProviders;

import com.learning.currencyprovider.dataProviders.api.APIResponse;
import org.springframework.stereotype.Component;

import java.util.Set;

public interface ICurrencyDataManager {
    void updateAvailableCurrencies();
    Set<String> getAvailableCurrencies();
    APIResponse getResponse(String baseCurrency, String quoteCurrency);
}
