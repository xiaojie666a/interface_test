package com.test.day01;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class getResponse {
    @Test
    public void getFirstRequest(){
        Response res = given().

                //设置请求：请求头、请求体....
                        when().
                        post("http://www.httpbin.org/post").
                        then().log().all().extract().response();
        //相应事件-->time()
        System.out.println(res.time());
        System.out.println(res.getHeader("Date"));
    }
    @Test
    public void getFirstRequest01(){
        String jsonData = "{\"mobile_phone\": \"18685854221\",\"pwd\": \"12345678\"}";
        Response res = given().

                //设置请求：请求头、请求体....
                        body(jsonData).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v1").
                        when().
                        post("http://api.lemonban.com/futureloan/member/login").
                        then().log().all().extract().response();
                        Object o = res.jsonPath().get("data.type");
                        System.out.println(o);
//                        System.out.println(res.jsonPath().get("data.id"));

    }
    @Test
    public void getFirstRequest02(){
//        String jsonData = "{\"mobile_phone\": \"18685854221\",\"pwd\": \"12345678\"}";
        Response res = given().

                //设置请求：请求头、请求体....
                        when().
                        get("http://www.httpbin.org/json").
                        then().log().all().extract().response();
//        Object o = res.jsonPath().get("slideshow.slides[0].type");
//        System.out.println(o);
        List<String> list = res.jsonPath().getList("slideshow.slides.title");
        for (String e:list
             ) {
            System.out.println(e);
        }

    }
    @Test
    public void getFirstRequest03(){
        Response res = given().

                //设置请求：请求头、请求体....
                        when().
                        get("http://www.baidu.com").
                        then().log().all().extract().response();
        String o = res.htmlPath().get("html.head.title");
        System.out.println(o);
        String str = res.htmlPath().get("html.head.meta[0].@http-equiv");
        System.out.println(str);
    }
    @Test
    public void getFirstRequest04(){
        Response res = given().

                //设置请求：请求头、请求体....
                        when().
                        get("http://www.httpbin.org/xml").
                        then().log().all().extract().response();
        String str = res.htmlPath().get("slideshow.slide[1].title");
        System.out.println(str);
    }
    @Test
    public void getFirstRequest05(){
        String jsonData = "{\"mobile_phone\": \"18685854221\",\"pwd\": \"12345678\"}";
        Response res = given().

                //设置请求：请求头、请求体....
                        body(jsonData).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                        when().
                        post("http://api.lemonban.com/futureloan/member/login").
                        then().log().all().extract().response();
        //1.获取id
        int id = res.jsonPath().get("data.id");
        //2.获取token
        String token = res.jsonPath().get("data.token_info.token");
//        System.out.println(id+"\t"+token);
//        /**
//         * 充值
//         */
//        String Recharge ="{\"member_id\":"+ id+",\"amount\": 1000}";
//        Response res1 = given().
//
//                //设置请求：请求头、请求体....
//                        body(Recharge).
//                        header("Content-Type", "application/json").
//                        header("X-Lemonban-Media-Type", "lemonban.v2").
//                        header("Authorization","Bearer "+token).
//                        when().
//                        post("http://api.lemonban.com/futureloan/member/recharge").
//                        then().log().all().extract().response();

    }
}
