package steps;


import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import entities.createSession;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;


import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;

public class authSteps {
    createSession reqBody = new createSession();
    private RequestSpecification request;
    private Response response;


    @Given("^I set request with \"([^\"]*)\" and \"([^\"]*)\"$")
    public void iSetRequestWithAnd(String username, String password) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        reqBody.setUsername(username);
        reqBody.setPassword(password);
    }

    @And("I use header auth")
    public void iUseHeaderAuth() {

        request = given().header("Content-Type", "application/json");
    }

    @When("I call  post request CreateToken")
    public void iCallPostRequestCreateToken() {
        response = request.when()
                .body(reqBody)
                .post("https://restful-booker.herokuapp.com/auth");

    }

    @Then("I get status code 200")
    public void iGetStatusCode() {
        String responseJson = response.then().extract().body().asString();
        String token = from(responseJson).get("token");

        System.out.println("=================================");
        System.out.println("El token es: "+token);
        System.out.println("=================================");
        Assert.assertNotNull(token);
        Assert.assertEquals(200,response.statusCode());

    }

}
