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

        //创建表
        //createTable(con);

        //删除表
        //dropTable(con);

        //插入/更新数据
        upsert(con);

        //查询数据
        //query(con);

        //删除数据
        //delete(con);



        stmt.close();
        con.close();
    }

    private static void query(Statement stmt) throws SQLException {
        ResultSet rs;
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
    }


    /**
     *
     */
    public static void query(Connection conn ) {

        try {

            // check connection
            if (conn == null) {
                System.out.println("conn is null...");
                return;
            }

            // create sql
            String sql = "select * from USER WHERE NAME = 'name13539'";
            String sql2 = "select /*+ INDEX(USER USER_INDEX_NAME ) */ * from USER WHERE NAME = 'name13539'";

            PreparedStatement ps = conn.prepareStatement(sql);
            long start  = System.currentTimeMillis();
            ResultSet rs = ps.executeQuery();

            System.out.println("id" + "\t" + "name" + "\t" + "age"+ "\t" + "city"+ "\t" + "sex");
            System.out.println("===========================================================");

            if (rs != null) {
                while (rs.next()) {
                    System.out.print(rs.getString("id") + "\t");
                    System.out.print(rs.getString("name") + "\t");
                    System.out.print(rs.getString("age") + "\t");
                    System.out.print(rs.getString("city") + "\t");
                    System.out.println(rs.getString("sex"));
                }
            }
            System.out.println("耗时:" +( System.currentTimeMillis()-start));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 插入数据
     * @param conn
     */
    public static void upsert(Connection conn ) {

        try {
            // check connection
            if (conn == null) {
                System.out.println("conn is null...");
                return;
            }

            // create sql
            //String sql = "upsert into user(id, INFO.account, INFO.passwd) values('001', 'admin', 'admin')";
            String insert = "upsert into USER values('2000001','Tom',28,'ZhaoQin','male')";

            PreparedStatement ps = conn.prepareStatement(insert);

            // execute upsert
            String msg = ps.executeUpdate() > 0 ? "insert success..."
                    : "insert fail...";

            // you must commit
            conn.commit();
            System.out.println(msg);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 删除数据
     * @param conn
     */
    public static void delete(Connection conn) {

        try {
            // check connection
            if (conn == null) {
                System.out.println("conn is null...");
                return;
            }

            // create sql
            String sql = "delete from user where id='2000001'";

            PreparedStatement ps = conn.prepareStatement(sql);

            // execute upsert
            String msg = ps.executeUpdate() > 0 ? "delete success..."
                    : "delete fail...";

            // you must commit
            conn.commit();
            System.out.println(msg);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 创建表
     * @param conn
     */
    public static void createTable(Connection conn) {
        try {
            // check connection
            if (conn == null) {
                System.out.println("conn is null...");
                return;
            }

            // check if the table exist
            ResultSet rs = conn.getMetaData().getTables(null, null, "STUDNET",
                    null);
            if (rs.next()) {
                System.out.println("table user is exist...");
                return;
            }
            // create sql
            String sql = "CREATE TABLE STUDENT (id varchar PRIMARY KEY,INFO.account varchar ,INFO.passwd varchar)";

            PreparedStatement ps = conn.prepareStatement(sql);

            // execute
            ps.execute();
            System.out.println("create success...");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 删除表
     * name：drop
     * description：drop table
     */
    public static void dropTable(Connection conn) {

        try {

            // check connection
            if (conn == null) {
                System.out.println("conn is null...");
                return;
            }

            // create sql
            String sql = "drop table STUDENT";

            PreparedStatement ps = conn.prepareStatement(sql);

            // execute
            ps.execute();

            System.out.println("drop success...");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
