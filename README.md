# NinjaOne Backend Interview Project

This project contains [Instructions](INSTRUCTIONS.md) that must be read in order to perform NinjaOne's code assessment.
Also the project is configured to use an in-memory H2 database that is volatile. If you wish to make it maintain data on
application shut down, you can change the spring.database.jdbc-url to point at a file like `jdbc:h2:file:/{your file path here}`

## Starting the Application

Run the `BackendInterviewProjectApplication` class

Go to:
* http://localhost:8080/sample/1
* http://localhost:8080/sample/2

You should see results for both of these. The application is working and connected to the H2 database. 

## H2 Console 

In order to see and interact with your db, access the h2 console in your browser.
After running the application, go to:

http://localhost:8080/h2-console

Enter the information for the url, username, and password in the application.yml:

```yml
url: jdbc:h2:mem:localdb
username: sa 
password: password
```

You should be able to see a db console now that has the Sample Repository in it.

Type:

```sql
SELECT * FROM SAMPLE;
````

Click `Run`, you should see two rows, for ids `1` and `2`

### Suggestions

Feel free to remove or repurpose the existing Sample Repository, Entity, Controller, and Service. 

## NinjaOne Backend Resolution

This API was intended to first register device types, then register devices and services. Customer registration does not
require a specific order. Finally, linking devices and services to the customer. 
There are already some records to facilitate use, they can be seen in the get endpoints.

To simulate the linking of devices in the instruction, simply use the following json in the endpoint:
* `/customer/1/assign`

```json
[
  {
    "deviceId": 1,
    "services": [
      2, 4, 5
    ]
  },
  {
    "deviceId": 1,
    "services": [
      2, 5
    ]
  },
  {
    "deviceId": 2,
    "services": [
      3, 4, 5
    ]
  },
  {
    "deviceId": 2,
    "services": [
      3, 4, 5
    ]
  },
  {
    "deviceId": 2,
    "services": [
      3
    ]
  }
]
```

To change services assigned to a device and customer use `/customer/{customer-id}/assign/device-service` 
with id of the relationship resulting from assignment operation.
This operation will change all services, making it possible to add or delete all assigned services at the same time.

Example:
```
[
    {
        "deviceServiceId": 1,
        "services": [
            2, 4
        ]
    }
]
```

There are two endpoints for checking results, one of them `/customer/{customer-id}/calculate` stores the calculations
per device at runtime, making it faster, but if there are changes to the cost of the services this could be a problem.
The another `/customer/{customer-id}/calculate/dynamic` does a dynamic calculation by device but requires heavier processing.

It is also possible check the endpoint documentation at the url: `/swagger-ui/index.html#/`
