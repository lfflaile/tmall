package com.how2j.tmall.test;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class TestTmall {
	public static void main(String[] args){
		//׼������������ݣ�
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        try (
               Connection c = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/tmall_springboot?useUnicode=true&characterEncoding=utf8",
                        "root", "gyywangdi");
                Statement s = (Statement) c.createStatement();
        )
        {
            for (int i = 1; i <=10 ; i++) {
                String sqlFormat = "insert into category values (null, '���Է���%d')";
                String sql = String.format(sqlFormat, i);
                s.execute(sql);
            }
              
            System.out.println("�Ѿ��ɹ�����10�������������");
   
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
	}
}
