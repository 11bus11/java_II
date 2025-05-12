package library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

public final class DbUtil {
  // 1) your JDBC constants in one place
  private static final String URL  = 
    "jdbc:mysql://mysql-eebfafa-library1.j.aivencloud.com:27035/defaultdb?sslMode=REQUIRED";
  private static final String USER = "avnadmin";
  private static final String PASS = Secret.Password();

  // 2) prevent instantiation
  private DbUtil() {}

  // 3) a single method to get a Connection
  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASS);
  }

    static DataSource createDataSource() {
        String password = Secret.Password();
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(String.format("jdbc:mysql://address=(host=mysql-eebfafa-library1.j.aivencloud.com)(port=27035)(user=avnadmin)(password=%s)(ssl-mode=REQUIRED)/defaultdb", password));
        return ds;
    }
}
