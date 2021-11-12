cinema-spring-app
==============================

### Technology
- Kotlin and Spring-Boot framework
- Database: MongoDB

### Running
Using Gradle
- ```./gradlew bootRun``` - starts the spring-boot server
- ```./gradlew build``` - builds package
- ```./gradlew test``` - runs all tests

---

## Choices
### Database
For this application NoSQL database MongoDB was chosen. The reasons for that are as follows:
- The requirements for this project are not complete, the schema can easily be changed if needed.
- There are unknown number of requests the system should handle. MongoDB provides reasonable write times as well as very fast read times. Since it is a cinema app it is better to assume there are going to be a lot of reads from users and a few write requests from cinema owners. MongoDB works well in this situation (with properly designed documents with indexes).
- It can easily be scaled if needed by using replica sets and/or sharding.
### Connection timeouts
The only external service used in project is currently the OMDb API. There is little information about the expected response times (preferably the 99th percentile) to be found on their website, besides a short mention ```response times should be < 500ms```. Therefore initial timeouts were set (easily configurable if needed):
- connectTimeout: 100ms
- readTimeout: 500ms
### Cache
Since the key only supports a 1,000 daily request limit, a simple caching mechanism was configured.
If needed, the cache could hold the values indefinitely since details such as movie name, release date, etc. should never change.
However, if we want to accurately present the `imdbRating`, this information should be refreshed periodically.
As to how often, it depends on how accurate we want this information to be. Since the rating of a movie usually does not change that much after a movie premiere, the cache could be extended for longer than current 24 hours.

### Retryer
Since the request to fetch imdb details can fail for various reasons, a simple retryer was set up. In this example it simply repeats the requests 3x before giving up, but some better solutions with, e.g. exponential backoff could be implemented if needed.

---
## Future work
Since this is an example app, there was not enough time to properly set everything that would be useful in a production ready application. Below are mentions of future work to be done.

### Security
For simplicity there is no authentication/authorization done in this app. This could make sense if, e.g. the server was behind a secured VPN and there was a separate API Gateway for the actual users. So the final architecture may vary, but there is still more to do:
* Admin -> all the endpoints from `/api/admin/**` should be secured and authorized only to specific group od administrators that has access to modifying the shows (cinema owners).
* Audit -> there should ideally exist an audit collection that stores the history of all changes made by administrators.
* User -> for simplicity there are no accounts for the users, therefore even guests can leave a review of a movie (without a need to register). Although it depends on the needs, current implementation could certainly be improved as right now, one person can leave many ratings for the same movie.

### Metrics
In a production ready application metrics such as number of handled requests (in RPS) and response times (e.g. p90/p99/p999) should be collected to ensure the steady flow of application.
This could easily be achieved with `metrics-spring` (dropwizard) and saving the metrics to e.g. InfluxDB and displaying over Grafana if needed.
* Alerting: in bare minimum there should exist another server that checks whether this server is alive by periodically calling one of the endpoints (e.g. `/health`). Other metrics could also have some alerts specified, if for example there is a sudden spike of 5xx responses from one of the endpoints (could be setup in Grafana).

### Logging
For easier debugging there should be certain levels of logging (debug/info/error) configured within the server, by using e.g. log4j library. Ideally the logs should be collected and sent to external storage (or even e-mail) so that we can easily browse them in a running application.

### Tests
Finally, more tests could be written to check for some corner case situations like handling incorrect data or timeouts. Even in a simple application like this, there are still a lot of things that could go wrong.

