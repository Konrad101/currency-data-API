package com.learning.currencyprovider.dataProviders.api;

import org.json.JSONArray;

public interface IAPIConnector {
    JSONArray getResponse(String url);
}
