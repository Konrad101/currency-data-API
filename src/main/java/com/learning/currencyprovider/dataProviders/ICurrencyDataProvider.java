package com.learning.currencyprovider.dataProviders;

import com.learning.currencyprovider.dataProviders.api.APIResponse;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface ICurrencyDataProvider {
    void updateAvailableCurrencies();
    Set<String> getAvailableCurrencies();
    APIResponse getResponse(String baseCurrency, String quoteCurrency);
    String getSource();
}
