package com.lemon.util;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class PhoneRandomUtil {
    public static String getRandomPhone() {
        Random random = new Random();
        String phonePrefix = "134";
            //nextInt生成一个随机整数范围[0-)
            for (int i = 0; i < 8; i++) {
                int number = random.nextInt(10);
                phonePrefix = phonePrefix + number;
            }

                       return phonePrefix;
    }
    public static String getUnregisterPhone(){
        String phone = null;
        while (true){
            phone = getRandomPhone();
            //对比数据库
            String sql = "SELECT count(*) FROM member where mobile_phone = "+ phone;
            Object result = JDBCUtils.querySingleData(sql);
            if ((long)result == 0){
                //表示没有被注册，符合要求
                break;
            }else {
                //表示已经被注册，继续执行
                continue;
            }
        }
        return phone;
    }
}
