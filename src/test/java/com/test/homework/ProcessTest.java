package com.test.homework;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ProcessTest {

    @Test
    public void process(){
        //1.新增项目
        //(1)注册借款人
        String str1 = "{\"mobile_phone\": \"18399992226\",\"pwd\": \"12345678\",\"reg_name\": \"xiaoming\"}";

        Response resRegister = given().
                body(str1).
                header("Content-Type", "application/json").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                //设置请求：请求头、请求体....
                        when().
                        post("http://api.lemonban.com/futureloan/member/register").
                        then().extract().response();
        //获取到借款人的用户id
        String o1 = resRegister.jsonPath().get("data.mobile_phone");

        //(1)登录借款人
        String str2 = "{\"mobile_phone\":" +o1+",\"pwd\": \"12345678\"}";
        Response borrowerLogin = given().
                body(str2).
                header("Content-Type", "application/json").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                //设置请求：请求头、请求体....
                        when().
                        post("http://api.lemonban.com/futureloan/member/login").
                        then().extract().response();
        //获取借款人token
        String borrowerToken = borrowerLogin.jsonPath().get("data.token_info.token");
        //获取借款人id
        int borrowerId = borrowerLogin.jsonPath().get("data.id");

        //(2)借款人新增
        String str3 = "{\n" +
                "    \"member_id\":"+borrowerId+",\n" +
                "    \"title\":\"房贷\",\n" +
                "    \"amount\":100000,\n" +
                "    \"loan_rate\":12,\n" +
                "    \"loan_term\":12,\n" +
                "    \"loan_date_type\":1,\n" +
                "    \"bidding_days\":5\n" +
                "}";
        Response addProject = given().
                body(str3).
                header("Content-Type", "application/json").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                header("Authorization","Bearer "+borrowerToken).
                //设置请求：请求头、请求体....
                        when().
                        post("http://api.lemonban.com/futureloan/loan/add").
                        then().extract().response();
        //获取项目id
        int projectId = addProject.jsonPath().get("data.id");




        //2.管理员审核
        //(1).注册管理员
        String str4 = "{\"mobile_phone\": \"18399992223\",\"pwd\": \"12345678\",\"type\": 0,\"reg_name\": \"admin\"}";

        Response adminRegister = given().
                body(str4).
                header("Content-Type", "application/json").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                //设置请求：请求头、请求体....
                        when().
                        post("http://api.lemonban.com/futureloan/member/register").
                        then().extract().response();
        //获取到借款人的用户id
        String o2 = adminRegister.jsonPath().get("data.mobile_phone");

        //(2)登录管理员账号
        String str5 = "{\"mobile_phone\":" +o2+",\"pwd\": \"12345678\"}";
        Response adminLogin = given().
                body(str5).
                header("Content-Type", "application/json").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                //设置请求：请求头、请求体....
                        when().
                        post("http://api.lemonban.com/futureloan/member/login").
                        then().extract().response();
        //获取管理员的token
        String adminToken = adminLogin.jsonPath().get("data.token_info.token");

        //(3)审核项目
        String str6 = "{\"loan_id\":"+projectId+",\"approved_or_not\": true}";
        given().
                body(str6).
                header("Content-Type", "application/json").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                header("Authorization","Bearer "+adminToken).
                //设置请求：请求头、请求体....
                        when().
                        patch("http://api.lemonban.com/futureloan/loan/audit").
                        then().extract().response();

        //3.投资项目
        //(1.)注册投资人
        String str7 = "{\"mobile_phone\": \"18399992224\",\"pwd\": \"12345678\",\"reg_name\": \"jack\"}";

        Response investRegister = given().
                body(str7).
                header("Content-Type", "application/json").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                //设置请求：请求头、请求体....
                        when().
                        post("http://api.lemonban.com/futureloan/member/register").
                        then().extract().response();
        //获取到投资人的用户id
        String o3 = investRegister.jsonPath().get("data.mobile_phone");

       //(2)投资人登录
        String str8 = "{\"mobile_phone\":" +o3+",\"pwd\": \"12345678\"}";
        Response investLogin = given().
                body(str8).
                header("Content-Type", "application/json").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                //设置请求：请求头、请求体....
                        when().
                        post("http://api.lemonban.com/futureloan/member/login").
                        then().extract().response();
        //获取投资人的token
        String investToken = investLogin.jsonPath().get("data.token_info.token");
        //获取投资人id
        int investId = investLogin.jsonPath().get("data.id");

        //(3)投资人充值
        String str9 = "{\"mobile_phone\":" +investId+",\"pwd\": \"12345678\"}";
        given().
                body(str9).
                header("Content-Type", "application/json").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                header("Authorization","Bearer "+investToken).
                //设置请求：请求头、请求体....
                        when().
                        post("http://api.lemonban.com/futureloan/member/recharge").
                        then().extract().response();

        //(4)投资人投资
        String str10 = "{\n" +
                "    \"member_id\":"+investId+",\n" +
                "    \"loan_id\":"+projectId+",\n" +
                "    \"amount\":50000.00\n" +
                "}";
        given().
                body(str10).
                header("Content-Type", "application/json").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                header("Authorization","Bearer "+investToken).
                //设置请求：请求头、请求体....
                        when().
                post("http://api.lemonban.com/futureloan/member/recharge").
                then().extract().response();

    }
    public void test(){

        String str8 = "{\"mobile_phone\":\"18685854226\",\"pwd\": \"12345678\"}";
        Response investLogin = given().
                body(str8).
                header("Content-Type", "application/json").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                //设置请求：请求头、请求体....
                        when().
                        post("http://api.lemonban.com/futureloan/member/login").
                        then().extract().response();
    }

}
