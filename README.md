# Receipt API
Welcome to the Receipt API! This service processes shopping carts and returns a receipt. There are two versions of this API. Endpoint details are described below.

## Building and running the app
### Prerequisites
The following must be set up and configured properly on your computer:
* Maven 3.9.1+
* Java JDK 20+

### Running Unit Tests
To run unit tests, run the following command:

`mvn clean test`

### Build and run jar
1. To generate an executable jar file, run the following command in the root directory of this project.

`mvn clean package`

2. Run the jar file. You can use the following command.

`java -jar target/receipt-api-0.0.1-SNAPSHOT.jar`

3. The application is now running on localhost:8080. You can use `curl`, Postman, or similar application to hit the endpoints. The endpoints are described below.

## Endpoints
### POST /v1/checkout
Content-Type: application/json
Body:
`{
  "items": [
    {
      "itemName": "name of grocery item",
      "sku": 0,
      "isTaxable": false,
      "ownBrand": false,
      "price": 0.00
    }
  ]
}`

Response: 200 - OK
`{
    "subtotal": 0.0,
    "taxTotal": 0.0,
    "taxableSubtotal": 0.0,
    "grandTotal": 0.0
}`

400 - Bad Request

405 - Method not allowed

500 - Internal Server Error

Description: The v1 endpoint takes a list of items in the customer's shopping cart and provides a receipt containing the `subtotal`, `taxTotal`, `taxableSubtotal`, and `grandTotal`. Taxable items are determined based on the `isTaxable` field in the request body.

### POST /v2/checkout
Content-Type: application/json
Body:
`{
  "items": [
    {
      "itemName": "name of grocery item",
      "sku": 0,
      "isTaxable": false,
      "ownBrand": false,
      "price": 0.00
    }
  ]
}`

Response: 200 - OK
`{
    "subtotalBeforeDiscounts": 0.0,
    "discountTotal": 0.0,
    "subtotalAfterDiscounts": 0.0,
    "taxableSubtotalAfterDiscounts": 0.0,
    "taxTotal": 0.0,
    "grandTotal": 0.0
}`

400 - Bad Request

405 - Method not allowed

500 - Internal Server Error

Description: The v2 endpoint contains all of the functionality of the v2 endpoint. Additionally, this endpoint attempts to find a coupon associated with the grocery item. Coupons are defined in the `coupons.json` file in the resources folder of this repository.

## Future Improvements
* One time read from coupons.json file instead of each time an item is processed
* Script for building and running the app (potentially use docker to do this)
* Utilize library (like Jacoco) to check code coverage

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.5/reference/htmlsingle/#web)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

