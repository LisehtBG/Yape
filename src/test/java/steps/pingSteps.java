package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;


import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class pingSteps {

    private RequestSpecification request;
    private Response response;
    String url = "https://restful-booker.herokuapp.com/";
    
    @Given("^the API is up and running$")
    public void theAPIIsUpAndRunning() {
    }

    @When("^I call get request HealthCheck$")
    public void iCallGetRequestHealthCheck() {
          response = given()
                .get(url+"ping");

    }

    @Then("^I get message created$")
    public void iGetMessageCreated() {
        int status = response.statusCode();
        assertThat(status, equalTo(HttpStatus.SC_CREATED));
    }
}
