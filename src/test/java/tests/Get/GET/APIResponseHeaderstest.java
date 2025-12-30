package tests.Get.GET;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class APIResponseHeaderstest {

    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;

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

    @Test
    public void getHeaderstest(){

        APIResponse apiresponse = requestContext.get("https://gorest.co.in/public/v2/users");
        int statuscode = apiresponse.status();
        System.out.println("Response status code:"+statuscode);
        Assert.assertEquals(statuscode,200);

        Map<String, String> headersmap = apiresponse.headers();
        headersmap.forEach((k,v)-> System.out.println(k+ ":"+v));
        System.out.println("Total response headers:"+headersmap.size());
        Assert.assertEquals(headersmap.get("server"),"cloudflare");
        Assert.assertEquals(headersmap.get("content-type"),"application/json; charset=utf-8");

        System.out.println("==========================");
        List<HttpHeader> headerslist = apiresponse.headersArray();
        for (HttpHeader e:headerslist){
            System.out.println(e.name+ ":" +e.value);
        }
    }
}
