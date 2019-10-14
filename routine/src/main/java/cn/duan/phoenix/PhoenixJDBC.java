package cn.duan.phoenix;

import java.sql.*;

public class PhoenixJDBC {

    private static String driver = "org.apache.phoenix.jdbc.PhoenixDriver";

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Statement stmt = null;
        ResultSet rs = null;

        Connection con = DriverManager.getConnection("jdbc:phoenix:duan1,duan2,duan3:2181");
        stmt = con.createStatement();
        String sql = "select * from USER limit 5";
        String sql2 = "select * from USER WHERE NAME = 'name13539'";
        String sql3 = "select /*+ INDEX(USER USER_INDEX_NAME ) */ * from USER WHERE NAME = 'name13539'";
        rs = stmt.executeQuery(sql3);
        long start  = System.currentTimeMillis();
        while (rs.next()) {
            System.out.print("id:"+rs.getString("id"));
            System.out.println(",name:"+rs.getString("name"));
            System.out.println("----------------------------");
        }
        System.out.println("耗时:" +( System.currentTimeMillis()-start));
        stmt.close();
        con.close();
    }
}
