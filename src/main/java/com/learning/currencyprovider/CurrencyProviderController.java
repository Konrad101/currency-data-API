package com.learning.currencyprovider;

import com.learning.currencyprovider.dataProviders.ICurrencyDataProvider;
import com.learning.currencyprovider.dataProviders.api.APIResponse;
import io.github.bucket4j.Bucket;
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
    private final ICurrencyDataProvider currencyDataProvider;
    private final Bucket bucket;

    public CurrencyProviderController(@Qualifier("Complex") ICurrencyDataProvider dataProvider,
                                      @Autowired Bucket bucket) {
        currencyDataProvider = dataProvider;
        this.bucket = bucket;
    }

    @Cacheable(value = "recent-rates-cache", key = "'CurrencyPairCache'+#baseCurrency+#quoteCurrency",
            unless = "#result.statusCode != 429")
    @RequestMapping(value = "/currency_pair/{base_currency}-{quote_currency}", method = RequestMethod.GET)
    public ResponseEntity<APIResponse> getCurrencyData(@PathVariable(value = "base_currency") String baseCurrency,
                                                       @PathVariable(value = "quote_currency") String quoteCurrency) {
        if (isTooManyRequests()) {
            APIResponse response = currencyDataProvider.getResponse(baseCurrency, quoteCurrency);
            return ResponseEntity.ok(response);
        }

        return new ResponseEntity<>(
                new APIResponse(false, "Too many requests. The limit is 2 requests per second"),
                HttpStatus.TOO_MANY_REQUESTS);
    }

    private boolean isTooManyRequests() {
        return bucket.tryConsume(1);
    }
}
