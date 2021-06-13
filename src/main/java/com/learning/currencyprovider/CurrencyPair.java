package com.learning.currencyprovider;

import com.learning.currencyprovider.dataProviders.api.APIResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CurrencyPair extends APIResponse {
    private final String baseCurrency;
    private final String quoteCurrency;
    private final String dataSource;
    private final BigDecimal value;
    private final LocalDate valueDate;

    public CurrencyPair(int code, String message,
                        String baseCurrency, String quoteCurrency,
                        String dataSource, BigDecimal value, LocalDate valueDate) {
        super(code, message);
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.dataSource = dataSource;
        this.value = value;
        this.valueDate = valueDate;
    }

    public String getDataSource() {
        return dataSource;
    }

    public BigDecimal getValue() {
        return value;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }
}
