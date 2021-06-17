package com.learning.currencyprovider.dataProviders.api.connectors.nbp;

import com.learning.currencyprovider.CurrencyPair;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIConnector;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIDataProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class NbpCurrencyProvider implements IAPIDataProvider {
    private static final String BASE_API_CURRENCY = "PLN";

    private static final String RATES_TABLE_TYPE = "A";
    private static final String ALL_CURRENCIES_RECENT_RATES_URL = "https://api.nbp.pl/api/exchangerates/tables/" +
            RATES_TABLE_TYPE + "/today/";

    private final IAPIConnector apiConnector;

    private static Set<String> availableCurrencies;
    private static LocalDate lastUpdateDate;

    public NbpCurrencyProvider(@Qualifier("Http") IAPIConnector apiConnector) {
        this.apiConnector = apiConnector;
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
        if (availableCurrencies != null) {
            return availableCurrencies;
        }

        downloadAvailableCurrenciesFromAPI();
        return availableCurrencies;
    }

    private void downloadAvailableCurrenciesFromAPI() {
        // what if url is invalid?
        // what if there is no internet connection?
        JSONArray response = apiConnector.getResponse(ALL_CURRENCIES_RECENT_RATES_URL);
        JSONObject responseObject = (JSONObject) response.get(0);
        JSONArray rates = (JSONArray) responseObject.get("rates");

        availableCurrencies = new HashSet<>();
        for (int i = 0; i < rates.length(); i++) {
            JSONObject rate = (JSONObject) rates.get(i);
            availableCurrencies.add((String) rate.get("code"));
        }
        availableCurrencies.add(BASE_API_CURRENCY);
        lastUpdateDate = LocalDate.parse((String) responseObject.get("effectiveDate"));
    }

    @Override
    public LocalDate getDateOfMostRecentCurrencyRate(String baseCurrency, String quoteCurrency) {
        return null;
    }

    @Override
    public CurrencyPair getRecentCurrencyRate(String baseCurrency, String quoteCurrency) {
        return null;
    }

    /*public List<CurrencyPair> getCurrencyDataFromDatePeriod(Currencies currency, LocalDate from, LocalDate to) {
        List<CurrencyPair> currencyPairData = new ArrayList<>();
        JSONArray jsonArrayResponse;
        for (int i = 0; i < adjustedDates.size() - 1; i++) {
            LocalDate dateFrom = adjustedDates.get(i) == from ?
                    adjustedDates.get(i) :
                    adjustedDates.get(i).plusDays(1);
            jsonArrayResponse = apiConnector.getResponse(URL_BEGINNING + currency + "/"
                    + parseDateToUrlFormat(dateFrom) + "/"
                    + parseDateToUrlFormat(adjustedDates.get(i + 1)));
            currencyPairData.addAll(getCurrencyValuesFromJson(jsonArrayResponse));
        }

        return currencyPairData;
    }

    private List<CurrencyPair> getCurrencyValuesFromJson(JSONArray jsonArrayResponse) {
        List<CurrencyPair> currencyPairData = new ArrayList<>();
        if (jsonArrayResponse != null) {
            for (int i = 0; i < jsonArrayResponse.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArrayResponse.get(i);
                Currencies currencyCode = Currencies.valueOf(jsonObject.getString("code"));
                JSONArray rates = (JSONArray) jsonObject.get("rates");
                for (int j = 0; j < rates.length(); j++) {
                    JSONObject rateObj = (JSONObject) rates.get(j);
                    currencyPairData.add(createCurrencyFromRate(rateObj, currencyCode));
                }
            }
        }

        return currencyPairData;
    }

    private CurrencyPair createCurrencyFromRate(JSONObject rate, Currencies currencyCode) {
        if (rate == null) {
            return null;
        }

        return new CurrencyPair(
                BASE_API_CURRENCY,
                currencyCode,
                LocalDate.parse(rate.getString("effectiveDate")),
                rate.getDouble("mid"));
    }

    @Override
    public CurrencyPair getCurrencyDataFromDay(Currencies currency, LocalDate day) {
        List<CurrencyPair> currencyPairList = getCurrencyDataFromDatePeriod(currency, day, day);
        return currencyPairList.size() > 0 ? currencyPairList.get(0) : null;
    }

    private String parseDateToUrlFormat(LocalDate date) {
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return formatter.format(date);
        }

        return null;
    }
*/

}
