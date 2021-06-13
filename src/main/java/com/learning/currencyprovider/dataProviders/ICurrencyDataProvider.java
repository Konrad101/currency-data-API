package com.learning.currencyprovider.dataProviders;

import com.learning.currencyprovider.dataProviders.api.APIResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ICurrencyDataProvider {
    List<String> getAvailableCurrencies();
    APIResponse getResponse(String baseCurrency, String quoteCurrency);
    String getSource();
}
