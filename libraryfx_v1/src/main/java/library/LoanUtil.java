package library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
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

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
