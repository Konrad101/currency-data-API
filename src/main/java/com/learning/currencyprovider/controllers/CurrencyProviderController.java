package com.learning.currencyprovider.controllers;

import com.learning.currencyprovider.dataProviders.ICurrencyDataManager;
import com.learning.currencyprovider.dataProviders.api.APIResponse;
import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class CurrencyProviderController {
    private final ICurrencyDataManager currencyDataProvider;
    private final Bucket recentCurrencyPairBucket;
    private final Bucket updateCurrenciesBucket;

    public CurrencyProviderController(@Qualifier("APIProvider") ICurrencyDataManager dataProvider,
                                      @Qualifier("CurrencyPairLimiter") Bucket recentCurrencyPairBucket,
                                      @Qualifier("AvailableCurrenciesUpdateLimiter") Bucket updateCurrenciesBucket) {
        currencyDataProvider = dataProvider;
        this.recentCurrencyPairBucket = recentCurrencyPairBucket;
        this.updateCurrenciesBucket = updateCurrenciesBucket;
    }

    @Cacheable(value = "recent-rates-cache", key = "'CurrencyPairCache'+#baseCurrency+#quoteCurrency",
            unless = "#result.statusCodeValue == 429")
    @RequestMapping(value = "/currency_pair/{base_currency}-{quote_currency}", method = RequestMethod.GET)
    public ResponseEntity<APIResponse> getCurrencyData(@PathVariable(value = "base_currency") String baseCurrency,
                                                       @PathVariable(value = "quote_currency") String quoteCurrency) {
        if (recentCurrencyPairBucket.tryConsume(1)) {
            APIResponse response = currencyDataProvider.getResponse(baseCurrency, quoteCurrency);
            return ResponseEntity.ok(response);
        }

        return new ResponseEntity<>(
                new APIResponse(false, "Too many requests. The limit is 2 requests per second."),
                HttpStatus.TOO_MANY_REQUESTS);
    }

    @PutMapping(value = "/currency_pair/update_currencies")
    public ResponseEntity<APIResponse> updateAvailableCurrencies() {
        if (updateCurrenciesBucket.tryConsume(1)) {
            currencyDataProvider.updateAvailableCurrencies();
            return ResponseEntity.ok(new APIResponse(true, "Available currencies updated successfully."));
        }

        return new ResponseEntity<>(
                new APIResponse(false, "Available currencies were recently updated."),
                HttpStatus.TOO_MANY_REQUESTS);
    }
}
