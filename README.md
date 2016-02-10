# JavaTraining

TrainingAssignment

This project has 3 services module :

1.ProductCatalogue microservice

2.ProductPricing microservice

3.ClientService using node js

## What's needed?
 - The products and productprice  bucket
 - The _design/dev_by_type by_type view (built in products bucket)
 - The following map function should be copied in view

```
    function (doc, meta) {
		if (doc.productType) {
			emit(doc.productType, doc.description);
		}
	}
```
 - _design/dev_get_all_docs get_all view in poducts bucket
 - The following map function should be copied in view
 ```
    function (doc, meta) {
		emit(meta.id, null);
	}
```

## Building and running
configure  the application for your couchbase installation by editing **`src/main/resources/application.yml`**.

ProductCatalogue service is built using DropWizard and Gradle and currently using embedded jetty server running on port 8080. To run ProductCatalogue service clean and run as gradle. 

pricing service is built using SpringBoot and Maven and currently using embedded tomcat server running on port 9090. To run this service , run Application.java or using Eclipse gradle plugin .

ClientServices is built using Node.js. To run this service type node main.js from command prompt.


## REST API
===========================
For Product Catalog Service
===========================

Following are the end points of this service :

Adds a product (saving into couchbase using rxJava) asynchronously
POST

http://localhost:8080/products

body {"productType":"mobile", "description":"Some description of mobile.","brand":"Motorola","model":"Mot X Play","screenSize":"5.5 inch","updated":"2015-10-24 13:54:07"}


Retrieve the list of products based on simple search criteria (by creating and using a view in couchbase) Search criteria is Product Type
GET

http://localhost:8080/products?productType=mobile

Get at least ten products in a batch from couchbase (using RxJava) and expose an API for the same
GET

http://localhost:8080/products/batch?offset=0

Implement remove a product feature from the catalogue.
DELETE

http://localhost:8080/products/05e01471-cb7d-46b3-9b1e-5c08e34a5a69


===========================
For Product Pricing Service
===========================
Return the price of a given product.
GET

http://localhost:9090/productprices/05e01471-cb7d-46b3-9b1e-5c08e34a5a69

===========================
For Client Service
===========================

To get products and price information of particular product type together in a JSON format
GET

http://localhost:3000/getByType/productType=mobile


H1. Nice Docking
