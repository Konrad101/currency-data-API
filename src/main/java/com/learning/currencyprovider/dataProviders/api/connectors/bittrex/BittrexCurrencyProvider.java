package com.learning.currencyprovider.dataProviders.api.connectors.bittrex;

import com.learning.currencyprovider.CurrencyPair;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIConnector;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIDataProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class BittrexCurrencyProvider implements IAPIDataProvider {
    private static final String DATA_SOURCE = "Bittrex API";
    private static final String ALL_CURRENCIES_URL = "https://api.bittrex.com/api/v1.1/public/getcurrencies";

    private static Set<String> availableCurrencies;
    private static LocalDate lastUpdateDate;

    private final IAPIConnector apiConnector;


    public BittrexCurrencyProvider(@Qualifier("Http") IAPIConnector apiConnector) {
        this.apiConnector = apiConnector;
    }

    @Override
    public void updateAvailableCurrencies() {

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
        return null;
    }
}
