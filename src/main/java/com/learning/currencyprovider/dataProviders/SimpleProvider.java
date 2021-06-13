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
public class SimpleProvider implements ICurrencyDataProvider {
    @Override
    public List<String> getAvailableCurrencies() {
        return new ArrayList<>(Arrays.asList("USD", "PLN", "BTC"));
    }

    @Override
    public APIResponse getResponse(String baseCurrency, String quoteCurrency) {
        List<String> availableCurrencies = getAvailableCurrencies();
        BigDecimal value = new BigDecimal("3.29182030");
        if (availableCurrencies.contains(baseCurrency) && availableCurrencies.contains(quoteCurrency)) {
            return new CurrencyPair(baseCurrency, quoteCurrency,
                    getSource(), value,
                    LocalDate.now(), 0.12);
        }

        return new APIResponse(404, "Currency not available.");
    }

    @Override
    public String getSource() {
        return "Simple provider";
    }
}
