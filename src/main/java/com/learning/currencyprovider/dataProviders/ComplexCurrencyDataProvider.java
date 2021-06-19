package com.learning.currencyprovider.dataProviders;

import com.learning.currencyprovider.CurrencyPair;
import com.learning.currencyprovider.dataProviders.api.APIResponse;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIConnector;
import com.learning.currencyprovider.dataProviders.api.connectors.IAPIDataProvider;
import com.learning.currencyprovider.dataProviders.api.connectors.bittrex.BittrexCurrencyProvider;
import com.learning.currencyprovider.dataProviders.api.connectors.nbp.NbpCurrencyProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
@Qualifier("ComplexProvider")
public class ComplexCurrencyDataProvider implements ICurrencyDataProvider {
    private final List<IAPIDataProvider> apiProviders;


    public ComplexCurrencyDataProvider(@Qualifier("HttpConnector") IAPIConnector connector) {
        apiProviders = new ArrayList<>(Arrays.asList(
                new BittrexCurrencyProvider(connector),
                new NbpCurrencyProvider(connector))
        );
    }

    @Override
    public void updateAvailableCurrencies() {
        apiProviders.forEach(IAPIDataProvider::updateAvailableCurrencies);
    }

    @Override
    public Set<String> getAvailableCurrencies() {
        Set<String> availableCurrencies = new HashSet<>();
        apiProviders.forEach(provider -> availableCurrencies.addAll(provider.getAvailableCurrencies()));
        return availableCurrencies;
    }

    @Override
    public APIResponse getResponse(String baseCurrency, String quoteCurrency) {
        IAPIDataProvider baseCurrencyProvider = getProviderForCurrency(baseCurrency);
        IAPIDataProvider quoteCurrencyProvider = getProviderForCurrency(quoteCurrency);
        if (baseCurrencyProvider == null || quoteCurrencyProvider == null) {
            return getNotFoundResponse(baseCurrency, quoteCurrency);
        }

        CurrencyPair requestedCurrencyPair;
        if (baseCurrencyProvider == quoteCurrencyProvider) {
            requestedCurrencyPair = baseCurrencyProvider.getRecentCurrencyRate(baseCurrency, quoteCurrency);
        } else {
            String commonProvidersCurrency = getCommonProvidersCurrency(baseCurrencyProvider, quoteCurrencyProvider);
            if (commonProvidersCurrency == null) {
                return getNotFoundResponse(baseCurrency, quoteCurrency);
            }
            requestedCurrencyPair = getCurrencyValueFromProviders(
                    baseCurrencyProvider, quoteCurrencyProvider,
                    baseCurrency, quoteCurrency,
                    commonProvidersCurrency);
        }

        return requestedCurrencyPair;
    }

    private IAPIDataProvider getProviderForCurrency(String currency) {
        if (currency == null) {
            return null;
        }

        currency = currency.toUpperCase();
        for (IAPIDataProvider provider : apiProviders) {
            if (provider.getAvailableCurrencies().contains(currency)) {
                return provider;
            }
        }

        return null;
    }

    private CurrencyPair getCurrencyValueFromProviders(
            IAPIDataProvider baseCurrencyProvider, IAPIDataProvider quoteCurrencyProvider,
            String baseCurrency, String quoteCurrency, String commonCurrency) {
        CurrencyPair baseCommonCurrency = baseCurrencyProvider.getRecentCurrencyRate(baseCurrency, commonCurrency);
        CurrencyPair quoteCommonCurrency = quoteCurrencyProvider.getRecentCurrencyRate(commonCurrency, quoteCurrency);
        BigDecimal requestedCurrencyValue = baseCommonCurrency.getValue().multiply(quoteCommonCurrency.getValue());
        String dataSource = baseCommonCurrency.getDataSource() + ", " + quoteCommonCurrency.getDataSource();

        return new CurrencyPair(baseCurrency, quoteCurrency,
                dataSource, requestedCurrencyValue,
                baseCommonCurrency.getValueDate());
    }

    private APIResponse getNotFoundResponse(String baseCurrency, String quoteCurrency) {
        return new APIResponse(false, "Currency pair " + baseCurrency.toUpperCase() + "-"
                + quoteCurrency.toUpperCase() + " is not available.");
    }

    private String getCommonProvidersCurrency(IAPIDataProvider firstProvider, IAPIDataProvider secondProvider) {
        Set<String> firstProviderCurrencies = firstProvider.getAvailableCurrencies();
        for (String currency : firstProviderCurrencies) {
            if (secondProvider.getAvailableCurrencies().contains(currency)) {
                return currency;
            }
        }

        return null;
    }
}
