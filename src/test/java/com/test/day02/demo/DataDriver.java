package com.test.day02.demo;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

public class DataDriver {
    String num;
    String token;
    int borrowerId;
    BigDecimal decimal;
    @Test(priority=1,enabled = false)
    public void register() {

        //1.新增项目
        //(1)注册借款人
        String str1 = "{\"mobile_phone\": \"18399992284\",\"pwd\": \"12345678\",\"reg_name\": \"xiaoming\"}";

        Response resRegister = given().
                body(str1).
                header("Content-Type", "application/json").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                //设置请求：请求头、请求体....
                        when().
                        post("http://api.lemonban.com/futureloan/member/register").
                        then().log().all().extract().response();
        //获取到借款人的用户id
         num = resRegister.jsonPath().get("data.mobile_phone");
        String s = resRegister.jsonPath().get("data.mobile_phone");
        Assert.assertEquals(s,"18399992284");

    }
    @Test(priority = 2)
    public void login(){
        //RestAssured全局配置
        RestAssured.config=RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        RestAssured.baseURI="http://api.lemonban.com/futureloan";
        //(1)登录借款人
        String str2 = "{\"mobile_phone\":" + num + ",\"pwd\": \"12345678\"}";
        System.out.println(str2);
        String str = "{\"mobile_phone\": \"18399992278\",\"pwd\": \"12345678\"}";
        Response borrowerLogin = given().
                body(str).
                header("Content-Type", "application/json").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                //设置请求：请求头、请求体....
                        when().
                        post("/member/login").
                        then().log().all().extract().response();
        //获取借款人token
         token = borrowerLogin.jsonPath().get("data.token_info.token");
        //获取借款人id
         borrowerId = borrowerLogin.jsonPath().get("data.id");
        String strMsg = borrowerLogin.jsonPath().get("msg");
        Assert.assertEquals(strMsg,"OK");
        //通过金额来进行断言  restAssured如果返回的是json小数那么其类型一定是float类型的
        decimal = borrowerLogin.jsonPath().get("data.leave_amount");
        Assert.assertEquals(decimal, BigDecimal.valueOf(1000.21));
    }
    @Test(priority = 3)
    public void tuoUp(){
        String str2 = "{\n" +
                "\t\"member_id\": "+borrowerId+",\n" +
                "     \"amount\": 574.2}\n" +
                "}\n";
        Response res = given().
                body(str2).
                header("Content-Type", "application/json").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                header("Authorization", "Bearer " + token).
                //设置请求：请求头、请求体....
                        when().
                        post("/member/recharge").
                        then().log().all().extract().response();
        BigDecimal b = res.jsonPath().get("data.leave_amount");
        BigDecimal add = decimal.add(BigDecimal.valueOf(574.2));
        Assert.assertEquals(b,add);
    }
}
