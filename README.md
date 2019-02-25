# Mobile Subscriptions
REST API responsible for maintaining and managing mobile subscriptions, that are assigned to clients, along with some related information.


## Getting Started

### Prerequisites

- Maven (tested on 3.6.0)
- Java 11
- Docker (tested on 2.0.0.3 )

Make sure that you use the elasticsearch image provided in the docker-compose file located in the root directory of the repo, because its version match the spring boot data elasticsearch dependency declared in the pom file and because the mobile_subscription app container and elastics search container shoud run in the same docker network. They must be visible and accessible by its service name which is possible if they run in the same docker network.

### Start the app

1. Clone project from github repo https://github.com/JussV/mobiles.git

2. First run elasticsearch:
   
        docker-compose up elasticsearch 
   
   to start the elasticsearch container, which is a dependency for running and testing mobile_subscription app. 
   If the elasticsearch server is not fully started the app will fail to connect to it.
       
3. Then in the root folder of the project execute:

        mvn clean install

    to install maven dependencies, run tests and build a .jar file that will be used to build the docker image of the app.

4. Once the elasticsearch container is up and running, execute: 
    
        docker-compose up mobile-subscriptions


The app API is accessible on [http://localhost:8095/api/subscriptions](http://localhost:8095/api/subscriptions)

## REST API 
 - All endpoints and their responses are documented by using [Swagger](http://localhost:8095/swagger-ui.html#/).
 
 - Seed file is provided to insert initial customer data into database.
 
 - Search by mobile numbers is performed by using elastic search for several reasons like: instant search, manipulation of search results and saving the performance of our database.
 
 - Deletion is performed by using method DELETE and actually deleting the record from DB. Although, in reality we should not delete records, but update statuses. However, I am using deletion just to demonstrate "by the book" usage of DELETE verb. 
 
 - Deletion of Customer should delete all related mobile subscriptions, and that is the reason for using CascadeType.ALL.

## Sample data to test the REST API

1. Create Mobile Subscription

        {
            "msisdn": "+35677112233",
            "serviceType": "MOBILE_POSTPAID",
            "owner": {
                "id": 1002
            },
            "user": {
                "id": 1000
            }
        }

2. Update Mobile Subscription service type

        { "serviceType": "MOBILE_PREPAID" }

3. Update Mobile Subscription owner

        {
            "owner": {
                "id": 1001
            }
        }
        
4. Update Mobile Subscription User

        {
            "user": {
                "id": 1001
            }
        }
## Testing

To launch your application's tests, run:

    mvn clean test
    
Enjoy the app 🙂