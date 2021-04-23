package com.test.day02.demo;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

public class DataDrivenDemo {
    @Test(dataProvider = "getLoginData")
    public void login(String phone,String pwd,String assertion){
        //RestAssured全局配置
        RestAssured.config=RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        RestAssured.baseURI="http://api.lemonban.com/futureloan";
        //(1)登录借款人
        String str = "{\"mobile_phone\": \""+phone+"\",\"pwd\": \""+pwd+"\"}";
        String str1 = "{mobile_phone: '"+phone+"',pwd: '"+pwd+"'}";
        System.out.println(str1);
        Response res = given().
                body(str).
                header("Content-Type", "application/json").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                //设置请求：请求头、请求体....
                        when().
                        post("/member/login").
                        then().log().all().extract().response();
        String s = res.jsonPath().get("msg");
        System.out.println(s);
        Assert.assertEquals(s,assertion);
    }
    @DataProvider
    public Object[][] getLoginData(){
        Object[][] data = { {"15815541764","lemon666","账号信息错误"},
                            {"","lemon666","手机号码为空"},
                            {"18732146545","lemon666","账号信息错误"},
                            {"11012345645","lemon669","无效的手机格式"}};
        return data;
    }
}
