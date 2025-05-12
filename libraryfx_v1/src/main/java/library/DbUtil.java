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
        ds.setJdbcUrl(String.format("jdbc:mysql://address=(host=mysql-eebfafa-library1.j.aivencloud.com)(port=27035)(user=avnadmin)(password=%s)(ssl-mode=REQUIRED)/defaultdb", password));
        return ds;
    }
}
