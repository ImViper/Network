package chapter14;


import com.mysql.jdbc.Driver;

import java.io.IOError;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * description:
 * author:蔡诚杰;
 * date; 2020/12/7
 */
public class DBOperate1 {
    public static void main(String[] args) {
        //指定数据库所在位置，先用本地地址测试，访问本地的数据库
//        String dbUrl = "jdbc:mysql://202.116.195.71:3306/STUDENTDB1?characterEncoding=utf8&useSSL=false";
        String dbUrl="jdbc:mysql:///STUDENTDB2?serverTimezone=Hongkong";
        //指定用户名和密码
        String dbUser = "root";
        String dbPwd = "jimmycai";


//        Class.forName("com.mysql.jdbc.Driver");
        try {
            Class jdbcDriver = Class.forName("com.mysql.jdbc.Driver");
            java.sql.DriverManager.registerDriver((Driver) jdbcDriver.newInstance());
            //创建数据库连接对象
            Connection con = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
            //创建sql查询语句
            String sql = "select NO,NAME,AGE,CLASS from students where name like ? and age=?";
//            String sql = "select IP,NO,NAME,AGE,CLASS from students where name like ? and age=?";
            //创建数据库执行对象
            PreparedStatement stmt = con.prepareStatement(sql);
//设置sql语句参数
            stmt.setObject(1, "小%");
            stmt.setObject(2, 23);

            //从数据库的返回集合中读出数据
            ResultSet rs = stmt.executeQuery();

//循环遍历结果
            while (rs.next()) {
//不知道字段类型的情况下，也可以用rs.getObject(…)来打印输出结果
//                System.out.print(rs.getString(1) + "\n");
                System.out.print(rs.getString(1) + "\t");
                System.out.print(rs.getString(2) + "\t");
                System.out.print(rs.getInt(3) + "\t");
                System.out.print(rs.getString(4) + "\n");
            }
            System.out.println("------------------------------------");

//设置插入记录的sql语句(如何避免重复插入学号相同的信息？)
            sql = "insert into STUDENTS(IP,NO,NAME,AGE,CLASS) values(?,?,?,?)";
            stmt = con.prepareStatement(sql);
            stmt.setObject(1,"");
            stmt.setObject(2, "20181002946");
            stmt.setObject(3, "蔡诚杰");
            stmt.setObject(4, 20);
            stmt.setObject(5, "软件工程1804");

            stmt.executeUpdate();
//查询是否插入数据成功
            sql = "select NO,NAME,AGE,CLASS from STUDENTS ";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

//再次循环遍历结果，看是否成功
            while (rs.next()) {
                System.out.print(rs.getString(1) + "\t");
                System.out.print(rs.getString(2) + "\t");
                System.out.print(rs.getInt(3) + "\t");
                System.out.print(rs.getString(4) + "\n");
            }

//释放资源
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (con != null)
                con.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
    }
