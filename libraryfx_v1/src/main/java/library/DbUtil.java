package library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
}
