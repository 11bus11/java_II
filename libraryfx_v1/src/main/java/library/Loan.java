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
    ArrayList<Copy2> copiesLoaned;

    public Loan(int loanID, LocalDateTime borrowDate, User user, ArrayList<Copy2> copiesLoaned) {
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
            
            ResultSet resultSetLoan= connection.createStatement().executeQuery("select * from Loan");
            //ResultSet resultSetCopy= connection.createStatement().executeQuery("select * from LoanCopy");
            while(resultSetLoan.next()){
                int loanID = resultSetLoan.getInt("LoanID");
                LocalDateTime borrowDate = LocalDateTime.now();
                User user = findLoanUser(resultSetLoan.getInt("UserID"));
                // fundera --> 
                ArrayList<Copy2> copiesLoaned = findLoanCopy(resultSetLoan.getInt("LoanID"));
                Loan loan = new Loan(loanID, borrowDate, user, copiesLoaned);
                arrayLoans.add(loan);
                System.out.println(loan.loanID);
            }
                
        } catch (SQLException e) {
            e.printStackTrace();
        }  
        return arrayLoans;
    }

    //finding the user who made the loan
    public static User findLoanUser(int id) {
        User loanUser = null;
        for (User i : User.arrayUsersGlobal) {
            if (i.userID == id) {
                loanUser = i;
            }
        }
        return loanUser;
    }

    //finding the copies that were loaned
    public static ArrayList<Copy2> findLoanCopy(int id) {
        ArrayList<Copy2> loanCopy = new ArrayList<Copy2>();
        for (Copy2 i : Copy2.createCopies()) {
            if (i.copyID == id) {
                loanCopy.add(i);
            }
        }
        return loanCopy;
    }

    private static DataSource createDataSource() {
        String password = Secret.Password();
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(String.format("jdbc:mysql://address=(host=mysql-eebfafa-library1.j.aivencloud.com)(port=27035)(user=avnadmin)(password=%s)(ssl-mode=REQUIRED)/defaultdb", password));
        return ds;
    }
    
}
