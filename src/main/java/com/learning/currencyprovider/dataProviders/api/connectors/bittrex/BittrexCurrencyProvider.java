package com.learning.currencyprovider.dataProviders.api.connectors.bittrex;

import com.learning.currencyprovider.CurrencyPair;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIConnector;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIDataProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class BittrexCurrencyProvider implements IAPIDataProvider {
    private static final String DATA_SOURCE = "Bittrex API";
    private static final String BASE_API_CURRENCY = "BTC";

    private static final String ALL_CURRENCIES_URL = "https://api.bittrex.com/api/v1.1/public/getcurrencies";
    private static final String CURRENCY_PAIR_TICK_URL = "https://api.bittrex.com/api/v1.1/public/getticker?market=";

    private static Set<String> availableCurrencies;
    private static LocalDate lastUpdateDate;

    private final IAPIConnector apiConnector;


    public BittrexCurrencyProvider(@Qualifier("Http") IAPIConnector apiConnector) {
        this.apiConnector = apiConnector;
        if (availableCurrencies == null) {
            downloadAllCurrenciesFromAPI();
        }
    }

    @Override
    public void updateAvailableCurrencies() {
        availableCurrencies = null;
        if (lastUpdateDate != null && lastUpdateDate.isBefore(LocalDate.now())) {
            downloadAllCurrenciesFromAPI();
        }
    }

    @Override
    public Set<String> getAvailableCurrencies() {
        if (availableCurrencies == null) {
            downloadAllCurrenciesFromAPI();
        }

        return availableCurrencies;
    }

    private void downloadAllCurrenciesFromAPI() {
        JSONArray response = apiConnector.getResponse(ALL_CURRENCIES_URL);
        if (response == null || response.length() == 0) {
            return;
        }
        JSONObject responseObject = (JSONObject) response.get(0);
        JSONArray results = (JSONArray) responseObject.get("result");
        lastUpdateDate = LocalDate.now();
        availableCurrencies = new HashSet<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            availableCurrencies.add((String) result.get("Currency"));
        }
    }

    @Override
    public CurrencyPair getRecentCurrencyRate(String baseCurrency, String quoteCurrency) {
        if (baseCurrency.equalsIgnoreCase(quoteCurrency) ||
                !availableCurrencies.contains(baseCurrency.toUpperCase()) ||
                !availableCurrencies.contains(quoteCurrency.toUpperCase())) {
            return null;
        }
        JSONArray response = apiConnector.getResponse(CURRENCY_PAIR_TICK_URL +
                BASE_API_CURRENCY + "-" + baseCurrency);
        if (response == null || response.length() == 0) {
            return null;
        }
        BigDecimal baseCurrencyValue = getCurrencyValueInBaseCurrency(baseCurrency);
        BigDecimal quoteCurrencyValue = getCurrencyValueInBaseCurrency(quoteCurrency);
        if (baseCurrencyValue == null || quoteCurrencyValue == null) {
            return null;
        }
        BigDecimal currencyPairValue = baseCurrencyValue.divide(quoteCurrencyValue, RoundingMode.HALF_UP);

        return new CurrencyPair(baseCurrency, quoteCurrency, DATA_SOURCE, currencyPairValue, LocalDate.now());
    }

    private BigDecimal getCurrencyValueInBaseCurrency(String quoteCurrency) {
        if (quoteCurrency.equalsIgnoreCase(BASE_API_CURRENCY)) {
            return new BigDecimal("1.0000");
        }
        JSONArray response = apiConnector.getResponse(CURRENCY_PAIR_TICK_URL +
                BASE_API_CURRENCY + "-" + quoteCurrency);
        if (response == null || response.length() == 0) {
            return null;
        }
        JSONObject responseObject = response.getJSONObject(0);
        boolean success = responseObject.getBoolean("success");
        if (!success) {
            return null;
        }

        JSONObject result = responseObject.getJSONObject("result");
        return result.getBigDecimal("Last");
    }
}
