package com.learning.currencyprovider.dataProviders;

import com.learning.currencyprovider.CurrencyPair;
import com.learning.currencyprovider.dataProviders.api.APIResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
@Qualifier("Simple")
public class SimpleProviderManagerManager implements ICurrencyDataProvider {
    @Override
    public void updateAvailableCurrencies() {

    }

    @Override
    public Set<String> getAvailableCurrencies() {
        return new HashSet<>(Arrays.asList("USD", "PLN", "BTC"));
    }

    @Override
    public APIResponse getResponse(String baseCurrency, String quoteCurrency) {
        Set<String> availableCurrencies = getAvailableCurrencies();
        BigDecimal value = new BigDecimal("3.29182030");
        try {
            Thread.sleep(2500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
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
