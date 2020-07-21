## Campsite Reservation System

### Requirements

The full system requirements can be found in the enclosed `Coding-Challenge.pdf`

The following are my understanding of the requirements after talking with the stakeholder:

- 1 Month is treated as 30 days.

- Available period date-range from Day1 to Day2 implies both are inclusive, i.e. `2020-07-18 to 2020-07-18` is valid, as is often the case when you want to request one day off from your manager, you specify the starting date and the ending date for the same one.
  
- On the hand, for a user's desired reservation period, the arrival date is inclusive while the departure date is exclusive, i.e. this system treats it as invalid if the arrival date and the departure date are the same.
  Another example is if `2020-08-01 to 2020-08-04` is reserved, it indicates that the arrival date is `2020-08-01` while `2020-08-04` is the departure date, which is not reserved by the user, as he/she must leave by 12:00 AM of that day. 
  
- No more than one reservation can be made for the same date-range, even for overlapped date(s). For example, if `2020-08-01` to `2020-08-04` has been reserved, then each of `08-01`, `08-02`, and `08-03` is not available while `08-04` is available for the reason stated above.


### Solution

Create REST application to expose endpoints for:
 - Reporting the available dates of the requested range from the starting date and the ending date. The starting date defaults to one day from now while the ending date defaults to one month from now.
   
 - Creating a reservation if valid information is provided, which includes the user's full name, email address, arrival date, and departure date. The API has simple validation for user information: the first name and the last name must start with a letter while the email is validated using Apache email validator. The dates are strictly validated based on the requirements. Upon success, a unique reservation identifier along with additional infomration is provided.
   
 - Checking and cancelling a reservation with a valid reservation identifier.
 
 - Modifying a reservation with a valid reservation identifier along with new information.
 
 ### campsite-reservation-api
 
 - The default port: `9999`
 - To run the application using maven: `mvn spring-boot:run`
 - To run it from within IDE: the entry class is: `han.jiayun.campsite.reservation.Application`
 - To run the tests from maven: `mvn clean test` while the recommended way is to use IDE as it brings up more detailed test information.
 - After the application is running (and if you run it locally with the default port), you can see its full API definition using [Swagger UI at: ](http://http://localhost:9999/swagger-ui.html#/) http://localhost:9999/swagger-ui.html#/.
 
### Quality Assurance

- Totally 63 test cases were written, including unit tests, integration tests, and stress tests.
- It handles availability inquiries from 100,000 users concurrently with grace. When you run the test, it will hang on for a couple of seconds for this stress tests.
- It also tests that 1,000 users simultaneously reserve the same date-range and the system also handles it correclty: in the end, only one user will succeed in making the reservation while the others will fail in reserving the dates that are already taken. This is important: using an analogy, we don't want to let more than one paseenger to reserve the same flight seat!
- It also passed the tests of modifing and cancelling the created reservations correctly.
