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
        DataSource dataSource = DbUtil.createDataSource();
        ArrayList <Loan> arrayLoans = new ArrayList<Loan>();
            
        try (Connection connection = dataSource.getConnection()) {
            
            ResultSet resultSetLoan= connection.createStatement().executeQuery("select * from Loan");
            while(resultSetLoan.next()){
                int loanID = resultSetLoan.getInt("LoanID");
                LocalDateTime borrowDate = resultSetLoan.getTimestamp("BorrowDate");
                User user = findLoanUser(resultSetLoan.getInt("UserID"));
                ArrayList<Copy> copiesLoaned = findLoanCopy(resultSetLoan.getInt("LoanID"));
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
    //not working
    public static ArrayList<Copy> findLoanCopy(int id) {
        ArrayList<Copy> arrayCopy = new ArrayList<Copy>();
        DataSource dataSource = DbUtil.createDataSource();
   
        try (Connection connection = dataSource.getConnection()) {
            
            ResultSet resultSetLoanCopy= connection.createStatement().executeQuery("select * from Loan");
            while(resultSetLoanCopy.next()){
                int loanID = resultSetLoanCopy.getInt("LoanID");
                if (loanID == id) {
                    int copyID = resultSetLoanCopy.getInt("CopyID");
                    int index = 0;
                    for (Copy i : Copy.arrayCopiesGlobal) {
                        if (Copy.getCopyID(i) == copyID) {
                            arrayCopy.add(Copy.arrayCopiesGlobal.get(index));
                        }
                        index++;
                    }
                }
            
            } 
        } catch (Exception e) {
            System.out.println("error");
        }
                       
        return arrayCopy;
    }
    
    static ArrayList <Loan> arrayLoansGlobal = createLoans();
}

//LocalDateTime borrowDate = LocalDateTime.now();
