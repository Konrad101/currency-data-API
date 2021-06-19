package com.learning.currencyprovider.dataProviders;

import com.learning.currencyprovider.CurrencyPair;
import com.learning.currencyprovider.dataProviders.api.APIResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Qualifier("Simple")
public class SimpleProviderManagerManager implements IProviderManager {
    private List<String> getAvailableCurrencies() {
        return new ArrayList<>(Arrays.asList("USD", "PLN", "BTC"));
    }

    @Override
    public APIResponse getResponse(String baseCurrency, String quoteCurrency) {
        List<String> availableCurrencies = getAvailableCurrencies();
        BigDecimal value = new BigDecimal("3.29182030");
        if (availableCurrencies.contains(baseCurrency) && availableCurrencies.contains(quoteCurrency)) {
            return new CurrencyPair(baseCurrency, quoteCurrency,
                    getSource(), value,
                    LocalDate.now());
        }

        return new APIResponse(false, "Currency not available.");
    }

    private String getSource() {
        return "Simple provider";
    }
}
