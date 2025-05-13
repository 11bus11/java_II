package library;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

/**
 * Utility class that owns a single HikariCP connection-pool.
 * No instances are ever created – everything is static.
 */
public final class DbUtil {

    /** 
     * The one and only DataSource, initialised when the
     * class is first loaded (thread-safe by JVM spec). 
     */
    private static final DataSource DS = init();

    /**
     * Builds and configures the HikariDataSource.
     * This runs exactly once, inside the class initializer.
     */
    private static DataSource init() {
        // Pull the DB password from your secret-management helper
        String pw = Secret.Password();

        // Create a new HikariCP pool
        HikariDataSource ds = new HikariDataSource();

        //Set the JDBC URL
        ds.setJdbcUrl(String.format(
            "jdbc:mysql://address=(host=mysql-eebfafa-library1.j.aivencloud.com)"
          + "(port=27035)(user=avnadmin)(password=%s)(ssl-mode=REQUIRED)/defaultdb", pw));

        // (Optional) tweak pool size, timeouts, etc. here
        // ds.setMaximumPoolSize(10);
        // ds.setIdleTimeout(30_000);

        return ds;   // return the fully-configured pool
    }

    /**
     * Borrow a connection from the pool.
     * Caller **must** close it when done – closing simply returns the
     * connection to the pool, it does *not* hit the database.
     */
    public static Connection getConnection() throws SQLException {
        return DS.getConnection();
    }

    /**
     * Expose the underlying DataSource in case some framework
     * (e.g., JPA, MyBatis) prefers it to a raw Connection.
     * Package-private: only classes in `library` can see it.
     */
    static DataSource createDataSource() {
        return DS;
    }

    /**
     * Private constructor prevents accidental instantiation (You’re not supposed to create an object of it — you’re just meant to use its static methods):
     *     new DbUtil();  // compile-error – constructor is not visible
     */
    private DbUtil() {}
}
