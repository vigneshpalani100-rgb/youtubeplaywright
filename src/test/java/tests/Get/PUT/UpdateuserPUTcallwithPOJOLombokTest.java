package tests.Get.PUT;

import com.api.data.Users;
import com.api.data.user;
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

public class UpdateuserPUTcallwithPOJOLombokTest {

    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;
    static String emailid;

    @BeforeTest
    public void setup(){
        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();

    }

    @AfterTest
    public void tearDown(){

        playwright.close();
    }

    public static String getRamdomemail(){
        emailid = "testpwautomation"+ System.currentTimeMillis() + "@gmail.com";
        return emailid;
    }

    @Test
    public void createusertest() throws IOException {

        //POST call
        //create user object:using builder pattern
        Users users = Users.builder()
                .name("Ram Automation Lab00")
                .email(getRamdomemail())
                .gender("male")
                .status("active").build();


        //1.post call:create a user
        APIResponse apipostresponse = requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer f0e8a1429cb89e2c097b00a1bf8e695b1ec3df0a75d0fc6c30ebf9b8c3b56ab9")
                        .setData(users));

        System.out.println(apipostresponse.status());
        Assert.assertEquals(apipostresponse.status(), 201);
        Assert.assertEquals(apipostresponse.statusText(), "Created");
        String responsetext = apipostresponse.text();
        System.out.println(responsetext);

        //convert response text/json to pojo--deserialization
        ObjectMapper objectmapper = new ObjectMapper();
        user actuser = objectmapper.readValue(responsetext, user.class);

        System.out.println("actual user from the response------>");
        System.out.println(actuser);
//        System.out.println(actuser.getEmail());
        Assert.assertEquals(actuser.getName(),users.getName());
        Assert.assertEquals(actuser.getEmail(),users.getEmail());
        Assert.assertEquals(actuser.getStatus(),users.getStatus());
        Assert.assertEquals(actuser.getGender(),users.getGender());
        Assert.assertNotNull(actuser.getId());

        String userId = actuser.getId();
        System.out.println("new user id is:"+userId);

        //update status active to inactive
        users.setStatus("inactive");
        users.setName("Ram Automation playwrigt");

        System.out.println("-----------PUT call------------------------");

        //2.PUT call: update user
        APIResponse apiputresponse = requestContext.put("https://gorest.co.in/public/v2/users/" + userId,
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer f0e8a1429cb89e2c097b00a1bf8e695b1ec3df0a75d0fc6c30ebf9b8c3b56ab9")
                        .setData(users));
        System.out.println(apiputresponse.status()+":"+apiputresponse.statusText());
        Assert.assertEquals(apiputresponse.status(),200);

        String putResponsetext = apiputresponse.text();
        System.out.println("update user:"+putResponsetext);
        Users actputuser = objectmapper.readValue(putResponsetext, Users.class);

        Assert.assertEquals(actputuser.getId(),userId);
        Assert.assertEquals(actputuser.getStatus(),users.getStatus());
        Assert.assertEquals(actputuser.getName(),users.getName());

        //3.GET the updates user with get call

        System.out.println("----------------GET call---------------------------");
        APIResponse apigetresponse = requestContext.get("https://gorest.co.in/public/v2/users/"+ userId,
                RequestOptions.create()
                .setHeader("Authorization", "Bearer f0e8a1429cb89e2c097b00a1bf8e695b1ec3df0a75d0fc6c30ebf9b8c3b56ab9"));

        System.out.println(apigetresponse.url());
        int statuscode = apigetresponse.status();
        System.out.println("Response status code:"+statuscode);
        Assert.assertEquals(statuscode,200);
        Assert.assertEquals(apigetresponse.ok(),true);

        String statusGETstatustext = apigetresponse.statusText();
        System.out.println("Response status text:"+statusGETstatustext);

        String getResponseText = apigetresponse.text();

        Users actGETuser = objectmapper.readValue(putResponsetext, Users.class);
        Assert.assertEquals(actGETuser.getId(),userId);
        Assert.assertEquals(actGETuser.getStatus(),users.getStatus());
        Assert.assertEquals(actGETuser.getName(),users.getName());


    }
}
