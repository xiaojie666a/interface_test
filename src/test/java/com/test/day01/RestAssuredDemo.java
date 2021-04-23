package com.test.day01;

import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class RestAssuredDemo {
//    public static void main(String[] args) {
//        given().
//                //设置请求：请求头、请求体....
//        when().
//              get("https://www.baidu.com").
//        then().log().body();
//
//    }

    /**
     * get请求
     */
    @Test
    public void getFirstRequest(){
        given().
                queryParam("mobilePhone","18079036666").
                queryParam("pwd","123456").
                //设置请求：请求头、请求体....
        when().
              get("http://www.httpbin.org/get").
        then().log().body();
    }

    /**
     * 表单类post
     */
    @Test
    public void postRequest01(){
        given().
                formParam("mobilePhone","18079036666").
                formParam("pwd","123456").
                contentType("application/x-www-form-urlencoded").
                //设置请求：请求头、请求体....
                        when().
                post("http://www.httpbin.org/post").
                then().log().body();
    }

    /**
     * 参数为json类的post
     */
    @Test
    public void postRequest02(){
        String jsonData = "{\"mobilePhone\":\"18079036666\",\"pwd\":\"123456\"}";
        given().
                body(jsonData).
                contentType("application/json").
                //设置请求：请求头、请求体....
                        when().
                post("http://www.httpbin.org/post").
                then().log().body();
    }
    /**
     * 参数为xml的post
     */
    @Test
    public void postRequest03(){
        String xmlData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<suite>\n" +
                "<class>测试xml</class>\n" +
                "</suite>";
        given().
                body(xmlData).
                contentType("application/xml").
                //设置请求：请求头、请求体....
                        when().
                post("http://www.httpbin.org/post").
                then().log().body();
    }
    @Test
    public void postRequest04(){

        given().
              multiPart(new File("D:\\io\\1.jpg")).
                //设置请求：请求头、请求体....
                        when().
                post("http://www.httpbin.org/post").
                then().log().body();
    }
}
