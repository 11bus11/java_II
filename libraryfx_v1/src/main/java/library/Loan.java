package library;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
import javax.xml.transform.Result;

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

    //Create a loan in the system
    public static void createLoanNow(ArrayList<Copy> copies, User LoanUser) {
        DataSource dataSource = DbUtil.createDataSource();
        //sql for prepared statements
        String sqlLoan = "INSERT INTO Loan (UserID, BorrowDate) VALUES (?, CURRENT_TIMESTAMP())";
        String sqlLoanCopy = "INSERT INTO LoanCopy (LoanID, CopyID, IsReturned) VALUES (?, ?, False)";
        String sqlCopyStatus = ""; //skriva

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement psLoan = connection.prepareStatement(sqlLoan);
            PreparedStatement psLoanCopy = connection.prepareStatement(sqlLoanCopy);
            psLoan.setObject(1, LoanUser);
            ResultSet resultSet = psLoan.executeQuery();
            
            int newLoanID;
            int index = 0;
            while (copies.size() > index) {
                while (resultSet.next()) {
                    newLoanID = resultSet.getInt("LoanID");
                    psLoanCopy.setInt(1, newLoanID);
                
                }
                psLoanCopy.setInt(2, Copy.getCopyID(copies.get(index)));

                index++;
            }
            int loanID = newLoanID;
            Timestamp borrowDate = resultSet.getTimestamp("BorrowDate");
            User user = LoanUser;
            ArrayList<Copy> copiesLoaned = copies;
            Loan loan = new Loan(loanID, borrowDate, user, copiesLoaned);
            System.out.println(arrayLoansGlobal.size() + " before");
            Loan.arrayLoansGlobal.add(loan);
            System.out.println(arrayLoansGlobal.size() + " after");

            
            

        
        } catch (Exception e) {
            e.printStackTrace();
        }

        Timestamp borrowDate = resultSetLoan.getTimestamp("BorrowDate");
        User user = findLoanUser(resultSetLoan.getInt("UserID"));
        ArrayList<Copy> copiesLoaned = findLoanCopy(resultSetLoan.getInt("LoanID"), resultSetLoanCopy);
        Loan newLoan = new Loan(loanID, borrowDate, user, copiesLoaned);
        }
    }
}
