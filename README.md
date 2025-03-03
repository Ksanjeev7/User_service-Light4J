<<<<<<< HEAD
# User_service(Light4J)

- A microservice built using Light-4j that provides user management with MongoDB integration. This service follows RESTful principles and loads initial data from [JSONPlaceholder](https://jsonplaceholder.typicode.com/) select /users


user_service_ Built using Light -4J farmework and MongoDB
=======

# OpenAPI Light-4J Server

**Features**

* 6 REST APIs for user operations.
* MongoDB Integration (Auto-loads data on startup).
* Light-4j Framework for high-performance REST services.
* Validation & Error Handling.
*  Docker & Kubernetes Support.

## 📂 Project Structure
```
User_Service_Light4J/
│── src/
│   ├── main/
│   │   ├── java/com/swift/userservice/   # Java Source Code
│   │   │   ├── handler/                  # API Handlers
│   │   │   ├── model/                    # User Model Classes
│   │   │   ├── repository/               # MongoDB Repository
│   │   │   ├── database/                 # MongoDB Connection
│   │   ├── resources/
│   │   │   ├── config/                    # Configurations
│   │   │   ├── handler.yml                 # API Routes
│   │   │   ├── openapi.json                # OpenAPI Documentation
│   ├── test/java/com/swift/userservice/    # Test Cases
│── pom.xml        # Maven Dependencies
│── Dockerfile     # Docker Configuration
│── README.md      # Project Documentation
│── .gitignore     # Files to Ignore in Git
```

## ⚙️ Tech Stack
- **Java 11**
- **Light4J Framework**
- **MongoDB**
- **Maven**
- **Docker**

### Build and Start

The scaffolded project contains a single module. A fat jar server.jar will be generated in target directory after running the build command below.

```
./mvnw clean install -Prelease
```

With the fatjar in the server/target directory, you can start the server with the following command.

```
java -jar server/target/server.jar
```

To speed up the test, you can avoid the fat jar generation and start the server from Maven.

```
./mvnw clean install exec:exec
```

## 📌 Error Handling
| Error Code | Description |
|------------|------------|
| `404`     | User Not Found |
| `400`     | Invalid Input Data |
| `409`     | Conflict (User ID already exists) |
| `500`     | Internal Server Error |


### Test

By default, the OAuth2 JWT security verification is disabled, so you can use Curl or Postman to test your service right after the server is started. For example, the user_service API has the following endpoint.

```
curl -k https://localhost:8443/users
```

For your API, you need to change the path to match your specifications.

### Tutorial

To explore more features, please visit the [petstore tutorial](https://doc.networknt.com/tutorial/rest/openapi/petstore/).
>>>>>>> 37ebf1a (Initial Commit: Added user_Service with Ligjt4J)
