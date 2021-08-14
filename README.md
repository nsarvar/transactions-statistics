# transactions-statistics

A REST API for calculating statistics. The main use case for the API is to calculate realtime statistics for the
last `n` seconds of transactions. The API has the following endpoints:

### Create a new transaction

Called every time when transaction is made. It is also the sole input of this rest API.

**Request**

`POST /transactions`

Body

```json
{
  "amount": "12.3343",
  "timestamp": "2018-07-17T09:59:51.312Z"
}
```

Where:

- `amount` – transaction amount; a string of arbitrary length that is parsable as a BigDecimal
- `timestamp` – transaction time in the ISO 8601 format
  `YYYY-MM-DDThh:mm:ss.sssZ` in the UTC timezone (this is not the current timestamp)

**Response**

Returns empty body with one of the following statues:

- 201 – in case of success
- 204 – if the transaction is older than 60 seconds
- 400 – if the JSON is invalid
- 422 – if any of the fields are not parsable or the transaction date is in the future

### Get statistics

This endpoint returns the statistics computed on the transactions within the last 60 seconds.

`GET /statistics`

```json
{
  "sum": "1000.00",
  "avg": "100.53",
  "max": "200000.49",
  "min": "50.23",
  "count": 10
}
```

Where:

- `sum` – a BigDecimal specifying the total sum of transaction value in the last 60 seconds
- `avg` – a BigDecimal specifying the average amount of transaction value in the last 60 seconds
- `max` – a BigDecimal specifying single highest transaction value in the last 60 seconds
- `min` – a BigDecimal specifying single lowest transaction value in the last 60 seconds
- `count` – a long specifying the total number of transactions that happened in the last 60 seconds

All BigDecimal values always contain exactly two decimal places and use
`HALF_ROUND_UP` rounding. eg: 10.345 is returned as 10.35, 10.8 is returned as 10.80

### Delete transactions

This endpoint causes all existing transactions to be deleted The endpoint accepts an empty request body and return
a `204` status code.

`DELETE /transactions` 

## Implementation
- Java 11, Maven.
- The API is threadsafe for concurrent requests.
- The solution works without a database and also in-memory
  databases. It uses only data-structures.
- Service does not store all transactions in memory for all time.
- Transactions not necessary for correct calculation will be discarded.

## Testing

Run `mvn clean install` and `mvn clean integration-test` to test the service and API
