package library;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

public class Loan {
    int loanID;
    Timestamp borrowDate;
    User user;
    ArrayList<Copy> copiesLoaned;

    public Loan(int loanID, Timestamp borrowDate, User user, ArrayList<Copy> copiesLoaned) {
        this.loanID = loanID;
        this.borrowDate = borrowDate;
        this.user = user;
        this.copiesLoaned = copiesLoaned;
    }

    //creating the loans from database
    public static ArrayList<Loan> createLoans() {
        DataSource dataSource = DbUtil.createDataSource();
        ArrayList <Loan> arrayLoans = new ArrayList<Loan>();
            
        try (Connection connection = dataSource.getConnection()) {
            
            ResultSet resultSetLoan= connection.createStatement().executeQuery("select * from Loan");
            ResultSet resultSetLoanCopy= connection.createStatement().executeQuery("select * from LoanCopy");
            while(resultSetLoan.next()){
                int loanID = resultSetLoan.getInt("LoanID");
                Timestamp borrowDate = resultSetLoan.getTimestamp("BorrowDate");
                User user = findLoanUser(resultSetLoan.getInt("UserID"));
                ArrayList<Copy> copiesLoaned = findLoanCopy(resultSetLoan.getInt("LoanID"), resultSetLoanCopy);
                Loan loan = new Loan(loanID, borrowDate, user, copiesLoaned);
                arrayLoans.add(loan);
                System.out.println(loan.loanID);
            }
            resultSetLoan.close();
            resultSetLoanCopy.close();
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
    //not working
    public static ArrayList<Copy> findLoanCopy(int id, ResultSet resultSetLoanCopy) {
        ArrayList<Copy> arrayCopy = new ArrayList<Copy>();
        DataSource dataSource = DbUtil.createDataSource();
   
        try (Connection connection = dataSource.getConnection()) {
            
            while(resultSetLoanCopy.next()){
                int loanID = resultSetLoanCopy.getInt("LoanID");
                if (loanID == id) {
                    int copyID = resultSetLoanCopy.getInt("CopyID");
                    int index = 0;
                    while (Copy.arrayCopiesGlobal.size() > index) {
                        if (Copy.getCopyID(Copy.arrayCopiesGlobal.get(index)) == copyID) {
                            arrayCopy.add(Copy.arrayCopiesGlobal.get(index));
                        }
                        System.out.println(Copy.getCopyID(Copy.arrayCopiesGlobal.get(index)) + " " + copyID);
                        index++;
                    }
                }
            
            } 

        } catch (Exception e) {
            System.out.println("error findLoanCopy");
        }
                       
        return arrayCopy;
    }
    
    static ArrayList <Loan> arrayLoansGlobal = createLoans();
}
