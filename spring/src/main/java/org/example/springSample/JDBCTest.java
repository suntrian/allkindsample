package org.example.springSample;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCTest {

    private static ApplicationContext context;
    private static JdbcTemplate jdbcTemplate;

    static  {
        context = new ClassPathXmlApplicationContext("com/suntr/springSample/spring-jdbc.xml");
        jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");
    }

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = (DataSource) context.getBean("datasource");
        Connection connection = dataSource.getConnection();
        Assert.notNull(connection,"connection is null");
        Statement statement = connection.createStatement();
        String createTableSql = "CREATE TABLE COMPANY " +
                "(ID INT PRIMARY KEY     NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " AGE            INT     NOT NULL, " +
                " ADDRESS        CHAR(50), " +
                " SALARY         REAL)";
        statement.executeUpdate(createTableSql);

        String insertSql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                " VALUES (1, 'Paul', 32, 'California', 20000.00 );";
        statement.executeUpdate(insertSql);

        String querySql = "SELECT * FROM COMPANY;";
        ResultSet resultSet = statement.executeQuery(querySql);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");

            System.out.println("id:" + id + "name:" + name);
        }
        statement.close();
        connection.close();
    }
}
