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
import java.util.Map;

public class GETAPICall {

    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;

    @BeforeTest
    public void setup(){
        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();

    }

    @Test
    public void getSpecificuserAPItest() throws IOException {

        APIResponse apiresponse = requestContext.get("https://gorest.co.in/public/v2/users", RequestOptions.create()
                .setQueryParam("gender","female")
                .setQueryParam("status","active"));

        int statuscode = apiresponse.status();
        System.out.println("Response status code:"+statuscode);
        Assert.assertEquals(statuscode,200);
        Assert.assertEquals(apiresponse.ok(),true);

        String statustext = apiresponse.statusText();
        System.out.println("Response status text:"+statustext);

        System.out.println("---print api response withplain text----");
        System.out.println(apiresponse.text());

        System.out.println("---API Json response---");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(apiresponse.body());
        String jsonPrettyResponse = jsonResponse.toPrettyString();
        System.out.println(jsonPrettyResponse);
    }

    @Test
    public void getusersApiTest() throws IOException {

        APIResponse apiresponse = requestContext.get("https://gorest.co.in/public/v2/users");
        int statuscode = apiresponse.status();
        System.out.println("Response status code:"+statuscode);
        Assert.assertEquals(statuscode,200);
        Assert.assertEquals(apiresponse.ok(),true);

        String statustext = apiresponse.statusText();
        System.out.println("Response status text:"+statustext);

        System.out.println("---print api response withplain text----");
        System.out.println(apiresponse.text());

        System.out.println("---API Json response---");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(apiresponse.body());
        String jsonPrettyResponse = jsonResponse.toPrettyString();
        System.out.println(jsonPrettyResponse);

        System.out.println("---printAPI url---");
        System.out.println(apiresponse.url());

        System.out.println("---print response headers---");
        Map<String, String> headersmap=apiresponse.headers();
        System.out.println(headersmap);

        Assert.assertEquals(headersmap.get("content-type"),"application/json; charset=utf-8");
        Assert.assertEquals(headersmap.get("x-download-options"),"noopen");

    }

    @AfterTest
    public void tearDown(){

        playwright.close();
    }
}
