package com.lemon.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class ExcelPojo {
    @Excel(name = "序号(caseId)")
    private int caseId;
    @Excel(name = "接口模块(module)")
    private String module;
    private String title;
    @Excel(name = "请求头(requestHeader)")
    private String requestHeader;
    @Excel(name = "请求方式(method)")
    private String method;
    @Excel(name = "接口地址(url)")
    private String url;
    @Excel(name = "参数输入(inputParams)")
    private String inputParams;
    @Excel(name = "期望响应结果(expected)")
    private String expected;
    @Excel(name = "提取返回的数据(extract)")
    private String extract;
    @Excel(name = "数据库校验（dbAssert）")
    private String dbAssert;

}
