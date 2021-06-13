package com.learning.currencyprovider;

import com.learning.currencyprovider.dataProviders.SimpleProvider;
import com.learning.currencyprovider.dataProviders.api.APIResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
public class CurrencyProviderController {

    @RequestMapping(value = "/currency_pair/{base_currency}-{quote_currency}", method = RequestMethod.GET)
    public APIResponse getCurrencyData(@PathVariable(value = "base_currency") String baseCurrency,
                                       @PathVariable(value = "quote_currency") String quoteCurrency) {
        // object that will be able to decide to from what class get data
        // when there won't be such a class, then return null

        if (new Random().nextInt(2) % 2 == 0) {
            return null;
        }
        return new SimpleProvider().getResponse(baseCurrency, quoteCurrency);
    }
}
