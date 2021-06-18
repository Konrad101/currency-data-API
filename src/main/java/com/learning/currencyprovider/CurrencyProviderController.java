package com.learning.currencyprovider;

import com.google.common.util.concurrent.RateLimiter;
import com.learning.currencyprovider.dataProviders.ICurrencyDataProvider;
import com.learning.currencyprovider.dataProviders.api.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CurrencyProviderController {
    private final RateLimiter rateLimiter;
    private final ICurrencyDataProvider currencyDataProvider;

    public CurrencyProviderController(@Qualifier("Complex") ICurrencyDataProvider dataProvider,
                                      @Autowired RateLimiter rateLimiter) {
        currencyDataProvider = dataProvider;
        this.rateLimiter = rateLimiter;
    }

    @Cacheable(value = "recent-rates-cache", key = "'CurrencyPairCache'+#baseCurrency+#quoteCurrency")
    @RequestMapping(value = "/currency_pair/{base_currency}-{quote_currency}", method = RequestMethod.GET)
    public ResponseEntity<APIResponse> getCurrencyData(@PathVariable(value = "base_currency") String baseCurrency,
                                                       @PathVariable(value = "quote_currency") String quoteCurrency) {
        boolean readyToGo = rateLimiter.tryAcquire();
        if (readyToGo) {
            APIResponse response = currencyDataProvider.getResponse(baseCurrency, quoteCurrency);
            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK
            );
        }

        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }
}
