package com.lemon.testcase;

import com.alibaba.fastjson.JSON;
import com.lemon.common.BaseTest;
import com.lemon.data.Constant;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.JDBCUtils;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

public class investTest extends BaseTest {
    @BeforeClass
    public void setUp(){
        //生成三个角色的手机号码
        String member_phone = PhoneRandomUtil.getUnregisterPhone();
        String admin_phone = PhoneRandomUtil.getUnregisterPhone();
        String investPhone_phone = PhoneRandomUtil.getUnregisterPhone();
        //放进环境变量中
        Environment.envData.put("memberPhone",member_phone);
        Environment.envData.put("adminPhone",admin_phone);
        Environment.envData.put("investPhone",investPhone_phone);
        //读取用例数据从第一条~第九条
        List<ExcelPojo> list = readSpecialExcelData(3, 0, 9);
        for (int i = 0; i < list.size(); i++) {
            //先替换在发起请求
            ExcelPojo excelPojo = paramsReplace(list.get(i));
            //发送请求
            Response res = request(excelPojo,"invest");
            //判断是否要提取数据
            if (list.get(i).getExtract() != null){
                extractToEnvironment(excelPojo,res);

            }

        }
    }
    @Test
    public void test(){
        //RestAssured全局配置
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        RestAssured.baseURI = Constant.BaseUrl;
        //读取到第十条数据
        List<ExcelPojo> list = readSpecialExcelData(3, 9);
        //替换
        ExcelPojo excelPojo = paramsReplace(list.get(0));
        //发起请求
        Response res = request(excelPojo,"invest");
        //4.响应断言
        assertResponse(excelPojo,res);
        //5.数据库断言
        assertSql(excelPojo);


    }
    @AfterClass
    public void tearDown(){

    }
}
