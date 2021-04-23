package com.lemon.testcase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lemon.common.BaseTest;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.JDBCUtils;
import com.lemon.util.PhoneRandomUtil;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

public class RegisterTest extends BaseTest {
    @BeforeClass
    public void setup(){
      //堆积生成没有注册过的手机号码
        String phone = PhoneRandomUtil.getUnregisterPhone();
        String phone2 = PhoneRandomUtil.getUnregisterPhone();
        String phone3 = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("phone",phone);
        Environment.envData.put("phone1",phone2);
        Environment.envData.put("phone2",phone3);
    }
    @Test(dataProvider = "recharge")
    public void testRecharge(ExcelPojo excelPojo) throws FileNotFoundException {
        excelPojo = paramsReplace(excelPojo);
        Response res = request(excelPojo,"register");
        //4.断言
        assertResponse(excelPojo,res);
        //5.数据库断言
        assertSql(excelPojo);

    }
    @DataProvider
    public Object[] recharge(){

        List<ExcelPojo> list = readSpecialExcelData(0, 0);
        Object[] array = list.toArray();
        return array;
    }
}
