package com.learning.currencyprovider.controllers;

import com.learning.currencyprovider.dataProviders.IProviderManager;
import com.learning.currencyprovider.dataProviders.api.APIResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CurrencyProviderController {
    private final IProviderManager currencyDataProvider;

    public CurrencyProviderController(@Qualifier("Complex") IProviderManager dataProvider) {
        currencyDataProvider = dataProvider;
    }

    @RequestMapping(value = "/currency_pair/{base_currency}-{quote_currency}", method = RequestMethod.GET)
    public APIResponse getCurrencyData(@PathVariable(value = "base_currency") String baseCurrency,
                                       @PathVariable(value = "quote_currency") String quoteCurrency) {

        APIResponse response = currencyDataProvider.getResponse(baseCurrency, quoteCurrency);
        return response;
    }
}
