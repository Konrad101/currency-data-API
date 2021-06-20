# currency-data-API
REST API with currencies data using Spring Boot framework.

API provides most recent data about currencies depending on implemented data providers. 

## About the project

### Built With

* Java SDK 11.0.2 

* Spring Boot 2.5.0

* [Maven](https://maven.apache.org)

### Unit tests created with

* JUnit 5

### Starting the app

To launch the app you need to type command as below

```bash
mvn spring-boot:run
```

By default the app will start at <http://localhost:8080>

### Explore REST API

#### Endpoint: currency_pair 

| Method | Url | Decription | Sample Valid Response | 
| ------ | --- | ---------- | --------------------------- |
| GET   | /currency_pair/{base_currency}-{quote_currency} | Getting recent currency pair rate | [JSON](#recent_rate) |
| PUT   | /currency_pair/update_currencies | Updating list of available currencies | [JSON](#update_currencies) |

### Sample valid JSON responses

##### <a id="recent_rate">Get recent rate for BTC-USD: /currency_pair/BTC-USD</a>
```json
{
  "success": true,
  "message": "OK",
  "baseCurrency": "BTC",
  "quoteCurrency": "USD",
  "dataSource": "Bittrex API",
  "value": 34614.05330564,
  "valueDate": "2021-06-20"
}
```

##### <a id="update_currencies">Update available currencies: /currency_pair/update_currencies</a>
```json
{
  "success": true,
  "message": "Available currencies updated successfully."
}
```

### API Limits

#### You can send requests about recent currency pair value 2 times per second.

#### You can update available currencies once every 4 hours.
