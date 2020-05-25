### Reference Documentation
Use case : 
A sample project which has the below implementation :


The project is divided into below applications :


**1) Web Application** 

For Web Application :
Kindly create a database as referred in master_schema.sql
and specify the user credentials in application-configuration.properties


* Kindly note that the application-configuration.properties is being used to override all the application properties possible in the project.








     ↓ Web : This modules serves as the controller of all the incoming requests , it has its security context being wrapped up  and validates all the input file incoming.
     ↓ Service : This module wraps the business logic that needs to be provided to the application like validating the request , performing operations and then returning the control to web    
     ↓ Integration : This module act as facade for all the third party integrations that are being done. thus segregating all the dependencies
     ↓ Dao : This module provides the interface for the database where in the file is saved , records are created for the files and these records are then updated with Geo Codes
     ↓ Commons : This module provides the basic application perspective like the rest processor for api , metrics utility to debug the application logs etc




**2) Scheduler Application** 

The scheduler application has a throttling of 50 tps (based on configuration properties can be modified)
All the request to the api gateway are made and below is the heirarchy of the application

     ↓ Scheduler : This modules serves as the orchestrator of scheduled crons for which entries are made in database and records are being fetched
     ↓ Service : This module wraps the business logic that needs to be provided to the application like validating the request , performing operations and then returning the control to web    
     ↓ Integration : This module act as facade for all the third party integrations that are being done. thus segregating all the dependencies
     ↓ Dao : This module provides the interface for the database where in the file is saved , records are created for the files and these records are then updated with Geo Codes
     ↓ Commons : This module provides the basic application perspective like the rest processor for api , metrics utility to debug the application logs etc



**2) Listener Application** 

The Listener application act as a facade to provide an async behaviour to the application and based on various topics it can be assume to provide a mimic behaviour of a state machine. 

     ↓ Listener : This modules serves as the orchestrator to provide the async behaviour.
     ↓ Service : This module wraps the business logic that needs to be provided to the application like validating the request , performing operations and then returning the control to web    
     ↓ Integration : This module act as facade for all the third party integrations that are being done. thus segregating all the dependencies
     ↓ Dao : This module provides the interface for the database where in the file is saved , records are created for the files and these records are then updated with Geo Codes
     ↓ Commons : This module provides the basic application perspective like the rest processor for api , metrics utility to debug the application logs etc



#### `To Run the application`

 `WebApplication`
1) Kindly create database as required or mentioned in SQL.
2) Override the database credentials in application properties .sql
3) Specify the  integration keys in application-configuration.properties to override all the existing key.
4) build the project and run the jar in web folder
5) java -jar the WebApplication along with spring active profile and path to the application properties and server port

and voila your web service is up and running

 `SchedulerApplication`
 Repeat the above steps but run the jar present in scheduler folder

 `ListenerApplication`
 Repeat the above steps but run the jar present in listener folder

and voila your scheduler service is up and running


`Below are the few optimisations that can be done`

1) Allow caching of the records being downloaded to a distributed cache rather than jvm cache which is being currently user (eh cache)
2) Add circuit breaker such as hysterix in case the api is failing 
4) A dedicated batch system can be expolited rather than scheduler.
5) Add vault to secure username and passowrds
6) Add complete java doc for each method that is being utilized.
7) Expose api for user ie for which user is being created.
8) For s2s call the gRPC or Jwt authentication to be used rather than userinfo authentication
9) A distributed locking to be enabled so that a file which is being currently added by a user is not added again until its current request expires or is completed.
10) Expose an api for insertion of a user rather than manually being done currently and giving proper permission for @Secured Annotion to work.

and many more... :)

Good Morning my friend. Hope you have a smooth ride in code.

