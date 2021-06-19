package com.learning.currencyprovider.dataProviders.api.connectors.nbp;

import com.learning.currencyprovider.CurrencyPair;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIConnector;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIDataProvider;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class NbpCurrencyProvider implements IAPIDataProvider {
    private static final String DATA_SOURCE = "NBP Web API";
    private static final String BASE_API_CURRENCY = "PLN";

    private static final String RATES_TABLE_TYPE = "A";
    private static final String ALL_CURRENCIES_RECENT_RATES_URL = "https://api.nbp.pl/api/exchangerates/tables/" +
            RATES_TABLE_TYPE + "/";

    private final IAPIConnector apiConnector;

    private static Set<String> availableCurrencies = null;
    private static LocalDate lastUpdateDate;

    public NbpCurrencyProvider(IAPIConnector apiConnector) {
        this.apiConnector = apiConnector;
        if (availableCurrencies == null) {
            downloadAvailableCurrenciesFromAPI();
        }
    }

    @Override
    public void updateAvailableCurrencies() {
        availableCurrencies = null;
        if (lastUpdateDate != null && lastUpdateDate.isBefore(LocalDate.now())) {
            downloadAvailableCurrenciesFromAPI();
        }
    }

    @Override
    public Set<String> getAvailableCurrencies() {
        if (availableCurrencies == null) {
            downloadAvailableCurrenciesFromAPI();
        }

        return availableCurrencies;
    }

    private void downloadAvailableCurrenciesFromAPI() {
        JSONArray response = getMostRecentResponse();
        JSONArray rates = (JSONArray) getKeyValueFromResponse(response, "rates");
        if (rates == null || rates.length() == 0) {
            return;
        }

        lastUpdateDate = LocalDate.now();
        availableCurrencies = new HashSet<>();
        for (int i = 0; i < rates.length(); i++) {
            JSONObject rate = (JSONObject) rates.get(i);
            availableCurrencies.add((String) rate.get("code"));
        }
        availableCurrencies.add(BASE_API_CURRENCY);
    }

    @Override
    public CurrencyPair getRecentCurrencyRate(String baseCurrency, String quoteCurrency) {
        if (baseCurrency.equalsIgnoreCase(quoteCurrency) ||
                !availableCurrencies.contains(baseCurrency.toUpperCase()) ||
                !availableCurrencies.contains(quoteCurrency.toUpperCase())) {
            return null;
        }
        JSONArray response = getMostRecentResponse();
        JSONArray rates = (JSONArray) getKeyValueFromResponse(response, "rates");
        String rateDate = (String) getKeyValueFromResponse(response, "effectiveDate");
        if (rates == null || rateDate == null) {
            return null;
        }

        BigDecimal baseCurrencyValue = getCurrencyValueInBaseCurrencyFromRates(rates, baseCurrency);
        BigDecimal quoteCurrencyValue = getCurrencyValueInBaseCurrencyFromRates(rates, quoteCurrency);

        return createCurrencyPair(baseCurrency, quoteCurrency,
                baseCurrencyValue, quoteCurrencyValue, LocalDate.parse(rateDate));
    }

    private JSONArray getMostRecentResponse() {
        LocalDate date = LocalDate.now();
        int daysToCheck = 30;
        JSONArray response = null;
        while (response == null && daysToCheck-- > 0) {
            response = apiConnector.getResponse(ALL_CURRENCIES_RECENT_RATES_URL +
                    date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            date = date.minusDays(1);
        }

        return response;
    }

    private CurrencyPair createCurrencyPair(String baseCurrency, String quoteCurrency,
                                            BigDecimal baseCurrencyValue, BigDecimal quoteCurrencyValue,
                                            LocalDate valueDate) {
        BigDecimal currencyPairValue = baseCurrencyValue.divide(quoteCurrencyValue, RoundingMode.HALF_UP);
        return new CurrencyPair(baseCurrency, quoteCurrency, DATA_SOURCE, currencyPairValue, valueDate);
    }

    private BigDecimal getCurrencyValueInBaseCurrencyFromRates(JSONArray rates, String quoteCurrency) {
        if (rates == null || rates.length() == 0) {
            return null;
        } else if (quoteCurrency.equalsIgnoreCase(BASE_API_CURRENCY)) {
            return new BigDecimal("1.00000000");
        }

        for (int i = 0; i < rates.length(); i++) {
            JSONObject rate = (JSONObject) rates.get(i);
            String currencyCode = (String) rate.get("code");
            if (quoteCurrency.equalsIgnoreCase(currencyCode)) {
                return (BigDecimal) rate.get("mid");
            }
        }

        return null;
    }

    private Object getKeyValueFromResponse(JSONArray response, String key) {
        if (response == null || response.length() == 0
                || key == null || key.length() == 0) {
            return null;
        }

        JSONObject responseObject = (JSONObject) response.get(0);
        if (responseObject.keySet().contains(key)) {
            return responseObject.get(key);
        }

        return null;
    }
}
