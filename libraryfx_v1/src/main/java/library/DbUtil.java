package library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

public final class DbUtil {
    //create datasource
    static DataSource createDataSource() {
        String password = Secret.Password();
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(String.format("jdbc:mysql://address=(127.0.0.1)(port=3306)(user=root)(password=%s)/tester2", password));
        return ds;
    }
}
