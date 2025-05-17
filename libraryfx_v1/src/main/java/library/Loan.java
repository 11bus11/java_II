package library;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

public class Loan {

    private final int       loanID;
    private final Timestamp loanDate;
    private final User      user;

    public Loan(int loanID, Timestamp loanDate, User user) {
        this.loanID   = loanID;
        this.loanDate = loanDate;
        this.user     = user;
    }

    public int       getLoanID () { return loanID;   }
    public Timestamp getLoanDate(){ return loanDate; }
    public User      getUser   () { return user;     }

    /** Copies associated via LoanCopy index. */
    public List<Copy> getCopies() {
        return LoanCopy.copiesByLoanID
                       .getOrDefault(loanID, List.of())
                       .stream()
                       .map(LoanCopy::getCopy)
                       .collect(Collectors.toList());
    }

    /* ---------- initial cache ---------- */
    private static ArrayList<Loan> createLoans() {
        ArrayList<Loan> list = new ArrayList<>();
        DataSource ds = DbUtil.createDataSource();

        try (Connection c = ds.getConnection();
             ResultSet rs = c.createStatement().executeQuery("SELECT * FROM Loan")) {

            while (rs.next()) {
                int uid = rs.getInt("UserID");
                User u  = User.arrayUsersGlobal.stream()
                              .filter(x -> x.getUserID() == uid)
                              .findFirst().orElse(null);

                list.add(new Loan(rs.getInt("LoanID"),
                                  rs.getTimestamp("BorrowDate"), u));
            }
        } catch (SQLException e) { e.printStackTrace(); }

        return list;
    }

    public static final ArrayList<Loan> arrayLoansGlobal = createLoans();

    public static Loan createAndAdd(int loanID, User user) {
        Loan l = new Loan(loanID, Timestamp.valueOf(LocalDateTime.now()), user);
        arrayLoansGlobal.add(l);
        return l;
    }

    /* ---------- for the receipts ---------- */
    static List<Copy> forReturnReceipt = new ArrayList<>();
    static Loan latestLoan = null;
}
