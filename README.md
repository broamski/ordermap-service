ordermap-service
===============

This application is a RESTful service that consumes semi-generic Postal Code address data, geocodes it using GeoNames data (http://www.geonames.org), and returns the results in JSON format.

The primary address data source for this service is Splunk, but it could easily be adapted to consume from additional services. This can be used as the back end
for eCommerce sites, or similar, to plot customer latitude and longitude on a map solution of their choice.

#### Requirements

This requires the Splunk Java SDK. All other dependencies are managed via Maven.

#### Execution

The following request returns a JSON object indicating:
+ Number of results returned
+ Time taken to fulfill request
+ JSON Array of Address Objects

http://localhost:8080/ordermap-service/orders

{
count: "2",
requestTimeMS: "0",
	zipCodeList: [
		{
			postalCode: "12345",
			latitude: "40.1234",
			longitude: "-100.5432",
			timestamp: "1382396806000"
		},
		{
			postalCode: "45678",
			latitude: "41.4321",
			longitude: "-93.9876",
			timestamp: "1382396805000"
		}
	]
}
