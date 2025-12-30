package booking;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class TokenTest {

    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;
    static String emailid;

    @BeforeTest
    public void setup() {
        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();

    }

    @AfterTest
    public void tearDown() {

        playwright.close();
    }

       @Test
    public void gettokenTest() throws IOException {

        //String Json
        String reqtokenJsonBody = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";

        //post call:create a token
        APIResponse apiPostTokenResponse = requestContext.post("https://restful-booker.herokuapp.com/auth",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(reqtokenJsonBody));


        System.out.println(apiPostTokenResponse.status());
        Assert.assertEquals(apiPostTokenResponse.status(), 200);
        Assert.assertEquals(apiPostTokenResponse.statusText(), "OK");
        System.out.println(apiPostTokenResponse.text());

        ObjectMapper objectmapper = new ObjectMapper();
        JsonNode postJsonresponse = objectmapper.readTree(apiPostTokenResponse.body());
        System.out.println(postJsonresponse.toPrettyString());

        //capture token from post json response
        String tokenID = postJsonresponse.get("token").asText();
        System.out.println("token id:" + tokenID);

        Assert.assertNotNull(tokenID);


    }
}