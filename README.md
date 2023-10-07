# Getting Started

## HOW TO RUN:

First you must compile:

`gradle build`

Then run the compiled jar:

`java -jar build/libs/Challenge-0.0.1-SNAPSHOT.jar`


## DEPENDENCIES AND VERSIONS:

Spring boot (data-jpa, starter-web and starter-test): 3.1.4

Java: 17

H2

## EXPLANATIONS:

### LeadController:

LeadController was built to receive the id of the Lead object, and it tries to convert it to a Prospect.
If successful it will return a json like the following:

`{
"valid": true,
"reason": null
}`

If the conversion fails it will return a json like the following:

`{
"valid": false,
"reason": "Data do not match"
}`

The reason field will have the reason of the fail.

### LeadService: 
LeadService is the main service, which is called from the LeadController. This service has its main method 
`convertLeadToProspect` which receives the Lead national id number. 

In this service we validate that this lead has not yet been converted to a prospect, in addition we validate if this 
identification number exists in our database in the Lead table, to later make two external calls in parallel, one to the 
national registry and the other to the judicial system. 

The first one is to obtain the data of that identification number and validate that our information matches the 
information of the person in the national registry. The second is to validate that the lead has no judicial records.
If both validations are successful, we will proceed to call the service that tells us how much the person's score is. 
If it is higher than 60 points we will convert it to a prospect and save the new prospect in the database.

Two CompletableFuture are being used to handle the parallelism between the two calls.


### NationalRegistryService:
NationalRegistryService was created for two things. The first is to simulate making a http call and the second is to 
compare the information in that supposed response with the information in our Lead object. Additionally, a `Threed.sleep` 
is being used to delay the response between 500 and 3000 milliseconds.


### JudicialService:
JudicialService was created to simulate the response to a http call querying whether a person has court records.
Additionally, a `Threed.sleep` is being used to delay the response between 500 and 3000 milliseconds.

### ProspectQualificationService:
ProspectQualificationService this service simulates the calculation of a lead score

### LeadRepository and ProspectRepository:
LeadRepository and ProspectRepository are the repositories of the Lead and Prospect entities.
Currently, the H2 database is being used.

### ProspectService:
ProspectService was created to validate the existence of a prospect given its national identification number.
It also provides the functionality to save a prospect in the database.



## DATA:

### data.sql:
The data.sql file was added to insert some records when running the application, this will allow us to run the http 
services with preloaded data in the database.

### Data to test:

If you use these national id numbers you will be able to see the different flows of the system.

* "1234567890" -> Will allow you to successfully convert a Lead into a Prospect.
* "9876543210" -> It will generate an error because there are no records in the national registry.
* "1112233445" -> Will generate an error because the score is less than 60.
* "5555666677" -> It will generate an error because the data we have in the database for that lead does not match the data provided by the national registry.
* "9999888877" -> Will generate an error because the lead has court records.

## HOW TO EXECUTE ENDPOINT:

To run the lead to prospect conversion endpoint you can use the following curl with the following json body

```shell
curl --location 'localhost:8080/leads/lead-to-prospect' \
--header 'Content-Type: application/json' \
--data '{
  "nationalIdNumber": "1234567890"
}
'
```

