package com.lemon.testcase;

import com.alibaba.fastjson.JSON;
import com.lemon.common.BaseTest;
import com.lemon.data.Constant;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

public class LoginTest extends BaseTest {
    @BeforeClass
    public void setup() {
        //生成一个随机手机号
        String phone = PhoneRandomUtil.getUnregisterPhone();
        //把电话号码保存到环境变量中
        Environment.envData.put("phone",phone);

        //1.前置条件
        //读取excel中第一条数据去生成已经注册的数据
        //excel文件路径
        List<ExcelPojo> excelPojo = readSpecialExcelData(1, 0, 1);
        //把环境变量中的号码进行替换
        ExcelPojo excelPojo1 = paramsReplace(excelPojo.get(0));
        Response response = request(excelPojo1,"login");
        //把数据保存到环境变量中
        extractToEnvironment(excelPojo1,response);
    }

    @Test(dataProvider = "getLoginData")
    public void login(ExcelPojo excelPojo) {
        //(1)登录借款人
        //1.1替换
        excelPojo = paramsReplace(excelPojo);
        //1.2登录
        Response res = request(excelPojo,"login");
        //(2)断言
        //2.1把期望值转化成map
        assertResponse(excelPojo,res);

    }

    @DataProvider
    public Object[] getLoginData() {
//        List<ExcelPojo> list = readAllExcelData("D:\\io\\1.xls", 1);
//        list.remove(0);
//        Object[] objects = list.toArray();
        List<ExcelPojo> list = readSpecialExcelData(1, 1);
        Object[] objects = list.toArray();
        return objects;
    }



}
