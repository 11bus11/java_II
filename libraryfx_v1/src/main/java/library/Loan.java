package library;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

public class Loan {
    int loanID;
    LocalDateTime borrowDate;
    User user;
    ArrayList<Copy> copiesLoaned;

    public Loan(int loanID, LocalDateTime borrowDate, User user, ArrayList<Copy> copiesLoaned) {
        this.loanID = loanID;
        this.borrowDate = borrowDate;
        this.user = user;
        this.copiesLoaned = copiesLoaned;
    }

    //creating the loans from database
    public static ArrayList<Loan> createLoans() {
        DataSource dataSource = createDataSource();
        ArrayList <Loan> arrayLoans = new ArrayList<Loan>();
            
        try (Connection connection = dataSource.getConnection()) {
            ResultSet resultSet = connection.createStatement().executeQuery("select * from User");
            ResultSet resultUser = connection.createStatement().executeQuery("select * from User");
            while(resultSet.next()){
                int loanID = resultSet.getInt("LoanID");
                LocalDateTime borrowDate = LocalDateTime.now();
                User user = resultSet.getString("UserID");
                String email = resultSet.getString("Email");
                String password = resultSet.getString("Password");
                String userType = resultSet.getString("UserType");
                User user = new User(loanID, borrowDate, user, copiesLoaned);
                arrayUsers.add(user);
                System.out.println(user.lastName);
            }
                
        } catch (SQLException e) {
            e.printStackTrace();
        }  
        return arrayLoans;
    }

    public static User findLoanUser(int id) {
        User loanUser;
        

        return loanUser;

    }

    private static DataSource createDataSource() {
        String password = Secret.Password();
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(String.format("jdbc:mysql://address=(host=mysql-eebfafa-library1.j.aivencloud.com)(port=27035)(user=avnadmin)(password=%s)(ssl-mode=REQUIRED)/defaultdb", password));
        return ds;
    }
    
}
