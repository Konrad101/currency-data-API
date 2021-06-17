package com.learning.currencyprovider.dataProviders;

import com.learning.currencyprovider.CurrencyPair;
import com.learning.currencyprovider.dataProviders.api.APIResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@Qualifier("Complex")
public class ComplexCurrencyDataProvider implements ICurrencyDataProvider {
    // class that will be able to decide to from what class get data
    // when there won't be such a class, then return null

    @Override
    public List<String> getAvailableCurrencies() {
        return null;
    }

    @Override
    public APIResponse getResponse(String baseCurrency, String quoteCurrency) {
        return new CurrencyPair(baseCurrency, quoteCurrency,
                getSource(), new BigDecimal("1.23456"),
                LocalDate.now());
    }

    @Override
    public String getSource() {
        return "Complex provider";
    }
}