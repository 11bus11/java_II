package library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.sql.DataSource;

/** Registers a new loan and keeps caches synchronized. */
public class LoanUtil {

    private static final String SQL_LOAN      = "INSERT INTO Loan (UserID, BorrowDate) VALUES (?, ?)";
    private static final String SQL_LOAN_COPY = "INSERT INTO LoanCopy (LoanID, CopyID) VALUES (?, ?)";

    public static void registerLoan(List<Copy> copies, User user){

        if(copies==null || copies.isEmpty() || user==null) return;

        DataSource ds = DbUtil.createDataSource();

        try(Connection conn = ds.getConnection()){
            conn.setAutoCommit(false);

            /* Loan */
            int loanID;
            try(PreparedStatement ps = conn.prepareStatement(SQL_LOAN, Statement.RETURN_GENERATED_KEYS)){
                ps.setInt(1, user.getUserID());
                ps.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                ps.executeUpdate();
                var rs = ps.getGeneratedKeys(); rs.next();
                loanID = rs.getInt(1);
            }

            /* LoanCopy */
            try(PreparedStatement ps = conn.prepareStatement(SQL_LOAN_COPY)){
                for(Copy c : copies){
                    ps.setInt(1, loanID);
                    ps.setInt(2, c.getCopyID());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();

            /* updates in-memory caches */
            Loan newLoan = Loan.createAndAdd(loanID, user);
            for(Copy c : copies)
                LoanCopy.add(new LoanCopy(newLoan.getLoanID(), c.getCopyID(), false));

            Loan.latestLoan = newLoan;

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void registerUpdate(Copy copy) {
        
    }

    /**
     * Returns the number of days overdue for a given borrow date and work type.
     * If not overdue, returns 0.
     */
    public static int getDaysOverdue(LocalDate borrowDate, String workType) {
        int allowedDays = getAllowedDaysByWorkType(workType);
        LocalDate dueDate = borrowDate.plusDays(allowedDays);
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        return daysOverdue > 0 ? (int) daysOverdue : 0;
    }

    /**
     * Returns the allowed days for a work type.
     */
    public static int getAllowedDaysByWorkType(String workType) {
        if (workType == null) return 30;
        switch (workType.toLowerCase()) {
            case "other literature":  return 30;
            case "course literature": return 14;
            case "movie":             return 7;
            default:                  return 30;
        }
    }

    public static LocalDate getDueDate(LocalDate borrowDate, String workType) {
        int allowedDays = getAllowedDaysByWorkType(workType);
        LocalDate dueDate = borrowDate.plusDays(allowedDays);

        return dueDate;
    }
}
