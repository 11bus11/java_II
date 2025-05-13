package library;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;


public final class DbUtil {

    private static final DataSource DS = init();

    private static DataSource init() {
        String pw = Secret.Password();
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(String.format(
            "jdbc:mysql://address=(host=mysql-eebfafa-library1.j.aivencloud.com)"
          + "(port=27035)(user=avnadmin)(password=%s)(ssl-mode=REQUIRED)/defaultdb", pw));
        return ds;
    }

    
    public static Connection getConnection() throws SQLException {
        return DS.getConnection();
    }

    
    static DataSource createDataSource() { return DS; }

    private DbUtil() {}
}
