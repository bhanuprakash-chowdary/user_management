# User Management and Journal System

This project consists of two microservices: User Service and Journal Service.

## REST API Endpoints

### User Service
- POST /api/users/registration - Register a new user
  {
   "userName": "Bhanuprakash",
   "userEmail": "ravillabhanu1507@gmail.com",
   "userPassword":"Bhanu@123",
   "userMobileNumber":"7995183828"
}

- GET /api/users/getUserDetailsById/{id} - Get user details
- PUT /api/users/updateUserDetails - Update user information
{
   "userId":2,
   "userName": "BhanuprakashChowdary",
   "userEmail": "rbchowdary507@gmail.com",
   "userPassword":"Bhanu@123456",
   "userMobileNumber":"7981411227"
}
- DELETE /api/users/deleteUserDetails/{id} - Delete a user



### Journal Service
- GET /api/journals - Retrieve journals
- GET /api/journals/{id} - Retrieve perticular journals

## How to Run and Test

1. Make sure you have Docker and Docker Compose installed.
2. Navigate to the project root directory.
3. Run `docker-compose up --build` to start all services.
4. The User Service will be available at `http://localhost:8080`
5. The Journal Service will be available at `http://localhost:8081`

To test the system:
1. Use a tool like Postman or curl to send requests to the API endpoints.
2. Register a new user by sending a POST request to `http://localhost:8080/api/users/registration`
3. Verify that the user event is logged and stored by the Journal Service.
4. Retrieve journals by sending a GET request to `http://localhost:8081/api/journals`
