package com.qa.api.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class APIDisposeTest {

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
    public void disposeResponseTest(){

        APIResponse apiresponse = requestContext.get("https://gorest.co.in/public/v2/users");
        int statuscode = apiresponse.status();
        System.out.println("Response status code:"+statuscode);
        Assert.assertEquals(statuscode,200);
        Assert.assertEquals(apiresponse.ok(),true);

        String statustext = apiresponse.statusText();
        System.out.println("Response status text:"+statustext);

        System.out.println("---print api response withplain text----");
        System.out.println(apiresponse.text());

        apiresponse.dispose();//will dispose only response body but status code will remain same
//        System.out.println("---print api response after dispose withplain text----");
//        System.out.println(apiresponse.text());

        try {
            System.out.println(apiresponse.text());
        }catch (PlaywrightException e){
            System.out.println("api response body is disposed");
        }

        int statuscode1 = apiresponse.status();
        System.out.println("Response status code after dispose:"+statuscode1);

        String statustext1 = apiresponse.statusText();
        System.out.println("Response status text:"+statustext1);

        System.out.println("response url:"+apiresponse.url());


        // Request 2:
        APIResponse apiresponse1 = requestContext.get("https://reqres.in/api/users/2",
                RequestOptions.create()
                        .setHeader("x-api-key", "reqres_6097fc936525439a9076998f4640cc7c"));

        System.out.println("Get response body of second request");
        System.out.println("Status code:"+apiresponse1.status());
        System.out.println("Response body:"+apiresponse1.text());
//        request context dispose
       requestContext.dispose();
//        System.out.println("Response1 body:"+apiresponse.text());
//        System.out.println("Response2 body:"+apiresponse1.text());
    }


    @AfterTest
    public void tearDown(){

        playwright.close();
    }
}