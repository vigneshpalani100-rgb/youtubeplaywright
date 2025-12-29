package com.qa.api.tests;

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

public class createUserPostcallwithPojoLomboktest {

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

        //create user object:using builder pattern
        Users users = Users.builder()
                .name("Ram Automation")
                .email(getRamdomemail())
                .gender("male")
                .status("active").build();


        //post call:create a user
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


    }
}
