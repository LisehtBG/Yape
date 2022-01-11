package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import entities.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
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
    BookingData bookingData = new BookingData();
    private RequestSpecification request;
    private Response response;
    String url = "https://restful-booker.herokuapp.com/booking/";

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
        System.out.println("----------------GetBookingIds---------------------");
        String response = request.get(url).then().extract().body().asString();
        List<Map> booking = from(response).get();
        System.out.println("Lista: "+booking);
        System.out.println("El cliente tiene "+booking.size()+" reservas");
        Assert.assertNotNull(booking);
    }

    @Given("^The booking id is \"([^\"]*)\"$")
    public void theBookingIdIs(String id) throws Throwable {
        bookingData.setId(id);

    }

    @When("^I call get request GetBooking$")
    public void iCallGetRequestGetBooking() {
        request = given()
                .pathParam("id",bookingData.getId());
    }

    @Then("^I get booking information$")
    public void iGetBookingInformation() {
        try {
            System.out.println("----------------GetBooking---------------------");
            request.get(url+"{id}").then().extract().body();
            response = request.get(url+"{id}");
            int status = response.statusCode();
            if (status != 200){
                assertThat(200,equalTo(HttpStatus.SC_OK));
                System.out.println("Test Failed");
                return;
            }
            String response1= response.then().extract().body().asString();
            String firstname = from(response1).get("firstname");
            int totalPrice = from(response1).get("totalprice");
            System.out.println("El response es el siguiente "+response1);
            System.out.println("El nombre es: "+firstname);
            assertThat(totalPrice,notNullValue());
        }catch (Exception e){
            System.out.println(e.getMessage());
            Assert.assertFalse(true);
        }



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
        System.out.println("----------------CreateBooking---------------------");
        String response = request
                .body(dataCreate)
               .post(url)
               .then()
                .extract().asString();
        String firstname = from(response).get("booking.firstname");
        assertThat(firstname,equalTo(dataCreate.getFirstname()));
        System.out.println("El response es el siguiente "+response);

    }

    @Given("^The client want update  data with  \"([^\"]*)\", \"([^\"]*)\",\"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"\\. \"([^\"]*)\"$")
    public void theClientWantUpdateDataWith(String firstname, String lastname, Integer totalprice, Boolean depositpaid, String checkin, String checkout, String additionalneeds) throws Throwable {
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



    @When("^I call  put request UpdateBooking$")
    public void iCallPutRequestUpdateBooking() {
        try {
            System.out.println("------------Update Booking--------------");
            response = given().header("Cookie", "token="+dataGlobal.getToken())
                    .contentType(ContentType.JSON)
                    .body(dataCreate)
                    .put(url+"2");

            int status = response.statusCode();
            if (status != 200){
                assertThat(200,equalTo(HttpStatus.SC_OK));
                System.out.println("Test Failed");
                return;
            }
            String responseUP = response.then()
                    .extract()
                    .body().asString();
            String firstname = from(responseUP).get("firstname");
            assertThat(firstname,equalTo(dataCreate.getFirstname()));
            System.out.println("El repsonse es el siguiente: "+responseUP);
        }catch (Exception e){
            System.out.println(e.getMessage());
            Assert.assertFalse(true);
        }



    }

    @Then("^I get boocking update$")
    public void iGetBoockingUpdate() {

    }

    @Given("^The client \"([^\"]*)\" and lastname \"([^\"]*)\" want to update booking with id  \"([^\"]*)\"$")
    public void theClientAndLastnameWantToUpdateBookingWithId(String firstname, String lastname, String id) throws Throwable {

        data.setFirstname(firstname);
        data.setLastname(lastname);
        bookingData.setId(id);


    }

    @When("^I call  path request PartialUpdateBooking$")
    public void iCallPathRequestPartialUpdateBooking() {
        request= given().pathParam("id",bookingData.getId())
                .header("Cookie", "token="+dataGlobal.getToken())
                .contentType(ContentType.JSON);
    }

    @Then("^I get Update booking information for client$")
    public void iGetUpdateBookingInformationForClient() {
        System.out.println("------------PartialUpdateBooking--------------");
        String response1 = request.body(data)
                .patch(url+"{id}")
                .then().extract().body().asString();
        String firstname = from(response1).get("firstname");
        assertThat(firstname,equalTo(data.getFirstname()));
        System.out.println("El repsonse es el siguiente: "+response1);
    }

    @Given("^The client have  \"([^\"]*)\" booking$")
    public void theClientHaveBooking(String id) throws Throwable {
        bookingData.setId(id);

    }

    @When("^I call delete request DeleteBooking$")
    public void iCallDeleteRequestDeleteBooking() {
        request= given().pathParam("id",bookingData.getId())
                .header("Cookie", "token="+dataGlobal.getToken())
                .contentType(ContentType.JSON);
        response = request.body(data)
                .delete(url+"{id}");
    }

    @Then("I get status code 201")
    public void iGetStatusCode() {
        System.out.println("---------DeleteBooking------------");
        try {
            assertThat(response.statusCode(),equalTo(HttpStatus.SC_CREATED));
        }catch (Exception e){
            Assert.assertFalse(true);
        }
    }
}



















