### Reference Documentation
Use case : 

`Single employee sync` : 
An employee is created and marked into sync_status : PENDING. this pending employee object is then pushed to kafka for syncing purpose.

An insertion into cron is made in which the scheduler pics the employee and sends the request to kafka for all the employee having sync status : PENDING.

Meanwhile at the kafka the api for syncing with payroll is invoked only for those records which have the sync status PENDING  and the status of the employee record is then set to 
sync_status SUCCESS. Even if the kafka is down or payroll is down , the exception is thrown and the corresponding employee is created on





`Discussing the bulk use case` : For bulk api a single excel upload can suffice for bulk insertions where in all the records inserted are validated and insertions
made in the Employee with file_id column.(table alteration to indicate it is entered via file).
these records will then be picked by scheduler and pushed to kafka for sync (same as single sync)


A sample project which has the below implementation :


The project is divided into below applications :


**1) Web Application** 

For Web Application :
Kindly create a database as referred in master_schema.sql
and specify the user credentials in application-configuration.properties


* Kindly note that the application-configuration.properties is being used to override all the application properties possible in the project.
* All Metrics are pushed by StatsD client and can be verified by TIG {Telegraf Influx Grafana}


     ↓ `Web` : This modules serves as the controller of all the incoming requests , it has its security context being wrapped up  and validates all the input file incoming.
     ↓ `Service` : This module wraps the business logic that needs to be provided to the application like validating the request , performing operations and then returning the control to web    
     ↓ `Integration` : This module act as facade for all the third party integrations that are being done. thus segregating all the dependencies
     ↓ `Dao` : This module provides the interface for the database where in the file is saved , records are created for the files and these records are then updated with Geo Codes
     ↓ `Commons` : This module provides the basic application perspective like the rest processor for api , metrics utility to debug the application logs etc




**2) Scheduler Application** 

The scheduler application has a throttling of 50 tps (based on configuration properties can be modified)
All the request to the api gateway are invoked and below is the hierarchy of the application

     ↓ `Scheduler` : This modules serves as the orchestrator of scheduled crons for which entries are made in database and records are being fetched
     ↓ `Service` : This module wraps the business logic that needs to be provided to the application like validating the request , performing operations and then returning the control to web    
     ↓ `Integration` : This module act as facade for all the third party integrations that are being done. thus segregating all the dependencies
     ↓ `Dao` : This module provides the interface for the database where in the file is saved , records are created for the files and these records are then updated with Geo Codes
     ↓ `Commons` : This module provides the basic application perspective like the rest processor for api , metrics utility to debug the application logs etc



**2) Listener Application** 

The Listener application act as a facade to provide an async behaviour to the application and based on various topics it can be assume to provide a mimic behaviour of a state machine. 

     ↓ `Listener` : This modules serves as the orchestrator to provide the async behaviour.
     ↓ `Service` : This module wraps the business logic that needs to be provided to the application like validating the request , performing operations and then returning the control to web    
     ↓ `Integration` : This module act as facade for all the third party integrations that are being done. thus segregating all the dependencies
     ↓ `Dao` : This module provides the interface for the database where in the file is saved , records are created for the files and these records are then updated with Geo Codes
     ↓ `Commons` : This module provides the basic application perspective like the rest processor for api , metrics utility to debug the application logs etc



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

1) Add circuit breaker such as hysterix in case the api is failing 
2) A dedicated batch system can be expolited rather than scheduler.
3) Add vault to secure username and passowrds
4) Add complete java doc for each method that is being utilized.
5) Expose api for user ie for which user is being created.
6) A distributed locking to be enabled so that a file (bulk upload) which is being currently added by a user is not added again until its current request expires or is completed.
7) Expose an api for insertion of a user rather than manually being done currently and giving proper permission for @Secured Annotation to work.

and many more... :)

Good Morning my friend. Hope you have a smooth ride in code.

