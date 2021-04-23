package com.lemon.testcase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lemon.common.BaseTest;
import com.lemon.data.Constant;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

public class RechargeTest extends BaseTest {

    @BeforeClass
    public void setup(){
      //1.前置条件
      //读取excel中前两条数据
        //生成一个随机手机号
        String phone = PhoneRandomUtil.getUnregisterPhone();
        //把电话号码保存到环境变量中
        Environment.envData.put("phone",phone);
        List<ExcelPojo> list = readSpecialExcelData( 2, 0, 2);
        //(1.注册接口)
        ExcelPojo excelPojo1 = paramsReplace(list.get(0));
        Response resRegister = request(excelPojo1,"reCharge");

        //提取接口返回对应的字段保存到环境变量中
        extractToEnvironment(excelPojo1,resRegister);
        //参数替换,替换{{phone}}
        ExcelPojo excelPojo = paramsReplace(list.get(1));

        //(2.登录接口)
        //获取extract的值
        Response resLogin = request(excelPojo,"reCharge");
        //将接口返回的数据存在环境变量中
        extractToEnvironment(excelPojo,resLogin);
    }
    @Test(dataProvider = "recharge")
    public void testRecharge(ExcelPojo excelPojo){
        //用例执行之前替换{{member_ID}}为环境变量中保存的对应的值
        //请求头转换成map
        excelPojo = paramsReplace(excelPojo);

        Response res = request(excelPojo,"reCharge");
        //4.断言
        assertResponse(excelPojo,res);
    }
    @DataProvider
    public Object[] recharge(){

        List<ExcelPojo> list = readSpecialExcelData(2, 2);
        Object[] array = list.toArray();
        return array;
    }



}
