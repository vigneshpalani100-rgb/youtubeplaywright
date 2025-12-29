package com.qa.api.tests;

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
import java.util.HashMap;

public class createuserAPIpostcalltest {

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

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("name","Rameh1");
        data.put("email",getRamdomemail());
        data.put("gender","male");
        data.put("status","active");

        //post call:create a user
        APIResponse apipostresponse = requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer f0e8a1429cb89e2c097b00a1bf8e695b1ec3df0a75d0fc6c30ebf9b8c3b56ab9")
                        .setData(data));

        System.out.println(apipostresponse.status());
        Assert.assertEquals(apipostresponse.status(), 201);
        Assert.assertEquals(apipostresponse.statusText(), "Created");
        System.out.println(apipostresponse.text());

        ObjectMapper objectmapper = new ObjectMapper();
        JsonNode postJsonresponse = objectmapper.readTree(apipostresponse.body());
        System.out.println(postJsonresponse.toPrettyString());

        //capture id from post json response
        String userid = postJsonresponse.get("id").asText();
        System.out.println("userid:"+userid);

        //GET call:Fetch the same user by id
        System.out.println("=============Get call response================");
        APIResponse apigetrespose = requestContext.get("https://gorest.co.in/public/v2/users/" + userid, RequestOptions.create()
                .setHeader("Authorization", "Bearer f0e8a1429cb89e2c097b00a1bf8e695b1ec3df0a75d0fc6c30ebf9b8c3b56ab9"));
        Assert.assertEquals(apigetrespose.status(),200);
        Assert.assertEquals(apigetrespose.statusText(),"OK");
        System.out.println(apigetrespose.text());
        Assert.assertTrue(apigetrespose.text().contains(userid));
        Assert.assertTrue(apigetrespose.text().contains("Rameh1"));
        Assert.assertTrue(apigetrespose.text().contains(emailid));
    }
}
