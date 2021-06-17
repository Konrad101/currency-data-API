package com.learning.currencyprovider.dataProviders;

import com.learning.currencyprovider.dataProviders.api.APIResponse;
import org.springframework.stereotype.Component;

@Component
public interface IProviderManager {
    APIResponse getResponse(String baseCurrency, String quoteCurrency);
}
