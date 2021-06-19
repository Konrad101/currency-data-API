package com.learning.currencyprovider.dataProviders.api.connectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletionException;

@Component
@Qualifier("HttpConnector")
public class HttpRequestConnector implements IAPIConnector {

    /**
     * Throws IllegalArgumentException when url is invalid
     *
     * @return null when url was valid but an error occurred in api (404 or sth)
     */
    @Override
    public JSONArray getResponse(String url) {
        if (url == null) {
            throw new IllegalArgumentException();
        }
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();
            String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .join();
            return createJSONArrayFromResponse(response);
        } catch (CompletionException ignored) {
        }
        return null;
    }

    private JSONArray createJSONArrayFromResponse(String response) {
        response = removeUnnecessaryWhiteSpaces(response);

        JSONArray jsonResponse;
        if (jsonResponseIsArray(response)) {
            jsonResponse = new JSONArray(response);
        } else {
            if (jsonResponseIsJsonObject(response)) {
                jsonResponse = new JSONArray();
                jsonResponse.put(new JSONObject(response));
            } else {
                jsonResponse = null;
            }
        }
        return jsonResponse;
    }

    public boolean jsonResponseIsArray(String response) {
        if (response != null) {
            return response.startsWith("[") && response.endsWith("]");
        }
        return false;
    }

    public boolean jsonResponseIsJsonObject(String response) {
        if (response != null) {
            // remove white spaces
            response = response.replaceAll("\\s", "");
            return response.startsWith("{") && response.endsWith("}");
        }
        return false;
    }

    private String removeUnnecessaryWhiteSpaces(String response) {
        if (response == null) {
            return null;
        }

        boolean endOfWhiteSpaces = false;
        int startIndex = 0;
        for (int i = 0; i < response.length() && !endOfWhiteSpaces; ++i) {
            if (!Character.isWhitespace(response.charAt(i))) {
                startIndex = i;
                endOfWhiteSpaces = true;
            }
        }

        int endIndex = response.length() - 1;
        endOfWhiteSpaces = false;
        for (int i = endIndex; i >= 0 && !endOfWhiteSpaces; --i) {
            if (!Character.isWhitespace(response.charAt(i))) {
                endIndex = i;
                endOfWhiteSpaces = true;
            }
        }

        return response.substring(startIndex, endIndex + 1);
    }
}
