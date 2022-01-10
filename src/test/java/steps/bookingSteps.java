package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import entities.BookingDates;
import entities.dataClient;
import entities.dataCreate;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class bookingSteps {

    dataClient data = new dataClient();
    dataCreate dataCreate = new dataCreate();
    private RequestSpecification request;
    private Response response;

    @Given("^The client \"([^\"]*)\" and lastname \"([^\"]*)\" have booking$")
    public void theClientAndLastnameHaveBooking(String firstname, String lastname) throws Throwable {
        data.setFirstname(firstname);
        data.setLastname(lastname);
    }

    @When("^I call  post request GetBookingIds$")
    public void iCallPostRequestGetBookingIds() {

        request = given()
                .contentType(ContentType.JSON)
                .queryParam("firstname",data.getFirstname())
                .queryParam("lastname",data.getLastname());

    }

    @Then("^The client has booking$")
    public void theClientHasBooking() {

        String url = "https://restful-booker.herokuapp.com/booking";
        String response = request.get(url).then().extract().body().asString();

        List<Map> booking = from(response).get();
        System.out.println("=======================================");
        System.out.println("Lista: "+booking);
        System.out.println("El cliente tiene "+booking.size()+" reservas");
        Assert.assertNotNull(booking);
    }

    @Given("^The booking id is \"([^\"]*)\"$")
    public void theBookingIdIs(String id) throws Throwable {
        data.setId(id);

    }

    @When("^I call get request GetBooking$")
    public void iCallGetRequestGetBooking() {

        System.out.println("El id: "+data.getId());
        request = given()
                .pathParam("id",data.getId()).log().all();

    }

    @Then("^I get booking information$")
    public void iGetBookingInformation() {

        String url = "https://restful-booker.herokuapp.com/booking/";
        String response = request.get(url+"{id}").then().extract().body().asString();
        String firstname = from(response).get("firstname");
        int totalPrice = from(response).get("totalprice");

        System.out.println("El response es el siguiente "+response);
        System.out.println("El nombre es: "+firstname);
        assertThat(firstname, equalTo("Eric"));
        assertThat(totalPrice,notNullValue());

    }

    @Given("^The client with date  \"([^\"]*)\", \"([^\"]*)\",\"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"\\. \"([^\"]*)\"$")
    public void theClientWithDate(String firstname, String lastname, Integer totalprice, Boolean depositpaid, String checkin, String checkout, String additionalneeds) throws Throwable {
        dataCreate.setFirstname(firstname);
        dataCreate.setLastname(lastname);
        dataCreate.setTotalprice(totalprice);
        dataCreate.setDepositpaid(depositpaid);
        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckin(checkin);
        bookingDates.setCheckout(checkout);
        dataCreate.setBookingdates(bookingDates);
        dataCreate.setAdditionalneeds(additionalneeds);
    }


    @When("^I call  post request CreateBooking$")
    public void iCallPostRequestCreateBooking() {

        request = given()
                .contentType(ContentType.JSON);


    }

    @Then("^I get booking information for client$")
    public void iGetBookingInformationForClient() {
        System.out.println(dataCreate);
        String response = request
                .body(dataCreate)
               .post("https://restful-booker.herokuapp.com/booking")
               .then().extract().asString();
        String firstname = from(response).get("booking.firstname");

        assertThat(firstname,equalTo(dataCreate.getFirstname()));
        System.out.println("=============================");
        System.out.println("The firstname is: "+firstname);

    }
}


















