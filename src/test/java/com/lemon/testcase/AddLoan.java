package com.lemon.testcase;

import com.lemon.common.BaseTest;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class AddLoan extends BaseTest {
    @BeforeClass
    public void setUp(){
        //1.生成一个随机号码
        String phone = PhoneRandomUtil.getUnregisterPhone();
        //2.把这个号码保存到环境变量中
        Environment.envData.put("phone",phone);
        //3.进行读取
        List<ExcelPojo> list = readSpecialExcelData(4, 0, 2);
        //4.进行参数替换
        ExcelPojo excelPojo = paramsReplace(list.get(0));
        //5.注册进行访问
        request(excelPojo,"addLoan");
        //6.替换
        ExcelPojo excelPojo1 = paramsReplace(list.get(1));
        Response res = request(excelPojo1,"addLoan");
        //7.把登录返回值替换到环境变量中
        extractToEnvironment(excelPojo1,res);


    }
    @Test
    public void test(){
        //1.读取
        List<ExcelPojo> list = readSpecialExcelData(4, 2);
        //2.进行替换
        ExcelPojo pojo = paramsReplace(list.get(0));
        //3.访问
        Response res = request(pojo,"addLoan");
        //4.断言
        assertResponse(pojo,res);
    }
}
