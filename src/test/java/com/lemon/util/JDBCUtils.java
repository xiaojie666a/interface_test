package com.lemon.util;

import com.lemon.data.Constant;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JDBCUtils {
    public static Connection getConnection() {
        //定义数据库连接
        //Oracle：jdbc:oracle:thin:@localhost:1521:DBName
        //SqlServer：jdbc:microsoft:sqlserver://localhost:1433; DatabaseName=DBName
        //MySql：jdbc:mysql://localhost:3306/DBName
        String url = "jdbc:mysql://"+ Constant.DB_URL+Constant.DB_NAME+"?useUnicode=true&characterEncoding=utf-8";
        String user = Constant.DB_USERNAME;
        String password = Constant.DB_PASSWORD;
        //定义数据库连接对象
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            return conn;
        }

    }

    public static void main(String[] args) {
        //1.获取连接对象
        Connection conn = getConnection();
        //2.实例化数据库操作对象
        QueryRunner qr = new QueryRunner();
//
        String sql = "UPDATE member set reg_name = '冬天' WHERE id = '1000359874'";
        update(sql);
//        //3.对数据库进行新增
//        try {
//            qr.update(conn, sql);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
        //4.查询数据库
        /**
        String sql_query = "select * from member where id = '1000359873'";
        try {
            Map<String, Object> map = qr.query(conn, sql_query, new MapHandler());
            System.out.println(map);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }**/
        //多个数据查询
//        String sql_query = "select * from member where id < 10";
//        try {
//            List<Map<String, Object>> list = qr.query(conn, sql_query, new MapListHandler());
//            for (Map<String, Object> e:list
//                 ) {
//                System.out.println(e);
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        //一条数据  比如统计
//        String sql = "select count(*) from member where id < 10";
//        try {
//            Long query = qr.query(conn, sql, new ScalarHandler<Long>());
//            System.out.println(query);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }


    }

    /**
     * 更新操作
     * @param sql
     */
    public static void update(String sql){
        QueryRunner qr = new QueryRunner();
        Connection conn = getConnection();
        try {
            qr.update(conn, sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

    }

    /**
     * 查询所有结果
     * @param sql
     * @return
     */
    public static List<Map<String, Object>> queryAll(String sql){
        QueryRunner qr = new QueryRunner();
        Connection conn = getConnection();
        List<Map<String, Object>> result = null;
        try {
            result = qr.query(conn, sql, new MapListHandler());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
            return result;
    }

    /**
     * 查询结果集中的第一条
     * @param sql
     * @return
     */
    public static Map<String, Object> queryOne(String sql){
        QueryRunner qr = new QueryRunner();
        Connection conn = getConnection();
        Map<String, Object> result = null;
        try {
            result = qr.query(conn, sql, new MapHandler());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return result;

    }

    /**
     * 查询单条数据
     * @param sql
     * @return
     */
    public static Object querySingleData(String sql){
        QueryRunner qr = new QueryRunner();
        Connection conn = getConnection();
        Object result = null;
        try {
            result = qr.query(conn, sql, new ScalarHandler<Object>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return result;

    }
}