package com.learning.currencyprovider.dataProviders.api.connectors;

import org.json.JSONArray;
import org.springframework.stereotype.Component;

@Component
public interface IAPIConnector {
    JSONArray getResponse(String url);
}
