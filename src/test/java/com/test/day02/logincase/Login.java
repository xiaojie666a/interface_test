package com.test.day02.logincase;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lemon.pojo.ExcelPojo;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

public class Login {
    @Test(dataProvider = "getLoginData01")
    public void login(ExcelPojo excelPojo){
        //RestAssured全局配置
        RestAssured.config=RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        RestAssured.baseURI="http://api.lemonban.com/futureloan";
        //(1)登录借款人
        //接口参数
        String str1 = excelPojo.getInputParams();
        //请求头
        String requestHeader = excelPojo.getRequestHeader();
       // System.out.println(title);
        Map map = JSON.parseObject(requestHeader);
        //请求地址
        String str3 = excelPojo.getUrl();
        //期望的响应结果
        String expected = excelPojo.getExpected();
        //把响应结果转成map
        Map expectMap = (Map) JSON.parse(expected);
        Set<String> set = expectMap.keySet();
        Response res = given().
                body(str1).
                headers(map).
                //设置请求：请求头、请求体....
                        when().
                        post(str3).
                        then().log().all().extract().response();
        for (String e:set
             ) {
            Object expect =  res.jsonPath().get(e);
            Object actual =  expectMap.get(e);
            Assert.assertEquals(actual,expect);

        }
    }
    @DataProvider
    public Object[][] getLoginData(){
        Object[][] data = { {"15815541764","lemon666","账号信息错误"},
                {"","lemon666","手机号码为空"},
                {"18732146545","lemon666","账号信息错误"},
                {"11012345645","lemon669","无效的手机格式"}};
        return data;
    }
    @DataProvider
    public Object[] getLoginData01(){
        //读取Excel
        File file = new File("D:\\io\\1.xls");
        //导入参数的对象
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(1);
        List<ExcelPojo> list = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        Object[] objects = list.toArray();
        return objects;
    }

//    public static void main(String[] args) {
//        //读取Excel
//        File file = new File("D:\\io\\1.xls");
//        //导入参数的对象
//        ImportParams importParams = new ImportParams();
//        importParams.setStartSheetIndex(1);
//        List<ExcelPojo> list = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
//        for (ExcelPojo e:list
//             ) {
//            System.out.println(e);
//        }
//    }

}
