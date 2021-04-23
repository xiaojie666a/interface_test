package com.lemon.common;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.lemon.data.Constant;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.JDBCUtils;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

public class BaseTest {

    @BeforeTest
    public void GlobalSetUp()  {
        //返回json为BigDecimal类型的数据
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //RestAssured全局配置
        RestAssured.baseURI = Constant.BaseUrl;

    }
    /**
     * 对get、post、patch等方法进行封装
     * @param excelPojo 每行数据对应的对象
     * @return 接口返回结果
     */
    public Response request(ExcelPojo excelPojo,String moduleName){
        //如果指定到输出到文件,那么设置重定向输出到文件相关的配置
        String logFilePath;
        if (Constant.LOG_TO_FILE){
            //为每一个请求单独的做日志保存
            File dirPath = new File(System.getProperty("user.dir")+"\\log\\"+moduleName);
            if (!dirPath.exists()){
                dirPath.mkdirs();
            }
            logFilePath = dirPath+"\\test"+excelPojo.getCaseId()+".log";
            PrintStream fileOutPutStream = null;
            try {
                fileOutPutStream = new PrintStream(new File(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutPutStream));
        }

        //接口请求地址
        String Url = excelPojo.getUrl();
        //接口请求方法
        String method = excelPojo.getMethod();
        //请求头
        String headers = excelPojo.getRequestHeader();
        //请求头转化成map
        Map<String,Object> map = JSON.parseObject(headers);
        //请求参数
        String params = excelPojo.getInputParams();
        //通过Response res接受
        Response res = null;
        //对get、post、put、patch
        if("get".equalsIgnoreCase(method)){
            if (params != null){
                res = given().log().all().headers(map).body(params).when().get(Url).then().log().all().extract().response();
            }else {
                res = given().log().all().headers(map).when().get(Url).then().log().all().extract().response();
            }
        }else if ("post".equalsIgnoreCase(method)){
            res = given().log().all().headers(map).body(params).when().post(Url).then().log().all().extract().response();
        }else if ("patch".equalsIgnoreCase(method)){
            res = given().log().all().headers(map).body(params).when().patch(Url).then().log().all().extract().response();
        }
        if (Constant.LOG_TO_FILE){
            try {
                Allure.addAttachment("接口请求响应信息", new FileInputStream(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return res;

    }

    /**
     * 对象相应结果进行断言
     * @param excelPojo
     * @param res
     */
    public void assertResponse(ExcelPojo excelPojo,Response res){
        //断言
        if (excelPojo.getExpected() !=null){
            Map<String,Object> map = JSON.parseObject(excelPojo.getExpected());
            for (String e:map.keySet()
            ) {
                //期望值
                Object expectValue = map.get(e);
                //实际值
                Object actualValue = res.jsonPath().get(e);
                //断言
                Assert.assertEquals(actualValue,expectValue);
            }
        }

    }

    /**
     * 数据库断言
     * @param excelPojo
     */
    public void assertSql(ExcelPojo excelPojo){
        String dbAssert = excelPojo.getDbAssert();;
        if (dbAssert != null){
            Map<String,Object> map = JSON.parseObject(dbAssert);
            Set<String> set = map.keySet();

            for (String key:set
            ) {
                Object expect = map.get(key);
                Object actual = JDBCUtils.querySingleData(key);
                if (expect instanceof BigDecimal){

                    Assert.assertEquals(actual,expect);
                }else if (actual instanceof Long){
                    //此时从excel中读取的Integer类型
                    //从数据库中读取的是long类型
                    Long expectValue = ((Integer) expect).longValue();
                    //断言
                    Assert.assertEquals(actual,expectValue);

                }else if (expect instanceof Integer){
                    Assert.assertEquals(actual,expect);
                }
            }
        }
    }
    /**
     *
     * @param sheetNum 读取的sheet编号
     */
    public List<ExcelPojo> readAllExcelData(int sheetNum){
        //读取Excel
        File file = new File(Constant.EXCEL_FILE_PATH);
        //导入参数的对象
        ImportParams importParams = new ImportParams();
        //读取第sheetNum个表
        importParams.setStartSheetIndex(sheetNum);
        List<ExcelPojo> list = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return list;
    }

    /**
     *
     * @param sheetNum 读取的sheet编号(索引从0开始)
     * @param startRow 从第几行开始(索引从0开始)
     * @param readRow 一共需要读取多少行
     * @return
     */
    public List<ExcelPojo> readSpecialExcelData(int sheetNum,int startRow,int readRow){
        //读取Excel
        File file = new File(Constant.EXCEL_FILE_PATH);
        //导入参数的对象
        ImportParams importParams = new ImportParams();
        //读取第sheetNum个表
        importParams.setStartSheetIndex(sheetNum);
        //设置读取的起始行
        importParams.setStartRows(startRow);
        importParams.setReadRows(readRow);
        List<ExcelPojo> list = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return list;
    }
    /**
     * 读取从指定行开始所有的数据
     * @param sheetNum 读取的sheet编号(索引从0开始)
     * @param startRow 从第几行开始(索引从0开始)
     * @return
     */
    public List<ExcelPojo> readSpecialExcelData(int sheetNum,int startRow){
        //读取Excel
        File file = new File(Constant.EXCEL_FILE_PATH);
        //导入参数的对象
        ImportParams importParams = new ImportParams();
        //读取第sheetNum个表
        importParams.setStartSheetIndex(sheetNum);
        //设置读取的起始行
        importParams.setStartRows(startRow);
        List<ExcelPojo> list = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return list;
    }

    /**
     * 将对应接口返回字段提取到环境变量中
     * @param excelPojo 提出返回字符串
     * @param response 接口返回Response对象
     */
    public void extractToEnvironment(ExcelPojo excelPojo,Response response){
        Map<String, Object> map = JSON.parseObject(excelPojo.getExtract());
        //遍历循环得到值
        for (String e:map.keySet()
        ) {
            Object path = map.get(e);
            Object value = response.jsonPath().get(path.toString());
            //存在环境变量中
            Environment.envData.put(e,value);
        }
    }

    /**
     * 从环境变量获取对应的值，进行正则替换
     * @param orgStr 原始值
     * @return 返回替换的值
     */
    public  String regexReplace(String orgStr){
        String result = orgStr;
        if(result !=null){
            Pattern pattern = Pattern.compile("\\{\\{(.*?)}}");
            //matcher:去匹配哪一个原始字符串，得到匹配对象
            Matcher matcher = pattern.matcher(result);

            while (matcher.find()){
                //group(0)表示获取到整个匹配到的内容
                String outStr = matcher.group(0);
                //group(1)表示获取到{{}}包裹的内容
                String innerStr = matcher.group(1);
//            System.out.println(innerStr);
                Object replaceStr = Environment.envData.get(innerStr);
                //replace
                result = result.replace(outStr, replaceStr+"");
        }
        }
        return result;
    }

    /**
     * 用例参数替换
     * @param excelPojo
     * @return excelPojo
     */
    public ExcelPojo paramsReplace(ExcelPojo excelPojo){

        //正则替换-->参数输入
        String inputParams = regexReplace(excelPojo.getInputParams());
        excelPojo.setInputParams(inputParams);
        //正则替换-->请求头
        String requestHeader = regexReplace(excelPojo.getRequestHeader());
        excelPojo.setRequestHeader(requestHeader);
        //正则替换-->接口地址
        String url = regexReplace(excelPojo.getUrl());
        excelPojo.setUrl(url);
        //正则替换-->期望的返回结果
        String expected = regexReplace(excelPojo.getExpected());
        excelPojo.setExpected(expected);
        //正则替换-->数据库替换
        String dbAssert = regexReplace(excelPojo.getDbAssert());
        excelPojo.setDbAssert(dbAssert);
        return excelPojo;
    }

}
