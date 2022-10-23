# Application
I have created a Kotlin Spring Boot application. The application can be run using Maven.
# Endpoint Design
- I have implemented a model view controller approach. The controller ClientIntegrationController.kt defines the path for the rest endpoint. It utilizes a UserDataService.kt service which constructs the response object and performs necessary business logic. The model objects for the response are defined in the model package and are comprised of the User's metadata (UserData.kt) and the list of git repositories (GitRepository.kt) that are owned by the user.  
- The service contains a configurable path so that client's path to data can be switched without code change. This also allows for a different client data endpoint to be called in test vs production code.
- The service has error handling has custom exceptions to handle non-existent users or repositories as well as 500 errors from the client integration.
- The service uses caching. I am using Spring's concurrent hashmap default cache. I have created a CacheService to handle time to live for this cache since the default cache does not support simple configuration for this as other third party caches do. Based on client requirements for how fresh cache data must be we can define the refresh policy. For the purposes of this test I am having it refresh each minute. 

# Test Design
Unit tests are available on the service layer. There is a unit test showing correct output when run with an existing client user. There are also tests demonstrating exception cases. A limitation of the current test design is that they depend on the Client's data api. If the API is down then that will block builds from completing. Also if data is changed on the test user this will be another trivial case where tests would fail. Perhaps the Client has a sandbox api and that could be called in the test classes so that the production api is not being called in the tests. Perhaps data would be more stable in the sandbox.

# Manual Testing
The following url can be used for testing:
http://localhost:8080/api/integration/data?user=octocat