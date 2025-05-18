package library;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/** Relates a Copy to an individual Loan. */
public class LoanCopy {

    private final int loanID;
    private final int copyID;
    private boolean  returned;

    /* ---------- constructor ---------- */
    public LoanCopy(int loanID, int copyID, boolean returned) {
        this.loanID  = loanID;
        this.copyID  = copyID;
        this.returned = returned;
    }

    /* ---------- getters ---------- */
    public int   getLoanID () { return loanID; }
    public int   getCopyID () { return copyID; }
    public boolean isReturned(){ return returned; }

    /** Finds the real Copy in the global cache. */
    public Copy getCopy() {
        return Copy.arrayCopiesGlobal.stream()
                    .filter(c -> c.getCopyID() == copyID)
                    .findFirst().orElse(null);
    }

    /* ---------- cache ---------- */
    public static final List<LoanCopy> arrayLoanCopiesGlobal = createLoanCopies();

    /** Fast index: loanID → list of LoanCopy */
    public static final Map<Integer, List<LoanCopy>> copiesByLoanID = buildIndex();

    private static ArrayList<LoanCopy> createLoanCopies() {
        ArrayList<LoanCopy> list = new ArrayList<>();
        DataSource ds = DbUtil.createDataSource();

        try (Connection c = ds.getConnection();
             ResultSet rs = c.createStatement().executeQuery("SELECT * FROM LoanCopy")) {

            while (rs.next()) {
                list.add(new LoanCopy(
                        rs.getInt ("LoanID"),
                        rs.getInt ("CopyID"),
                        rs.getBoolean("IsReturned")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private static Map<Integer, List<LoanCopy>> buildIndex() {
        Map<Integer, List<LoanCopy>> map = new HashMap<>();
        for (LoanCopy lc : arrayLoanCopiesGlobal)
            map.computeIfAbsent(lc.loanID, k->new ArrayList<>()).add(lc);
        return map;
    }

    /* ---------- util: adds new copy to cache ---------- */
    public static void add(LoanCopy lc){
        arrayLoanCopiesGlobal.add(lc);
        copiesByLoanID.computeIfAbsent(lc.loanID, k -> new ArrayList<>()).add(lc);
    }

    public static void remove(Copy c){
        for(LoanCopy lc : LoanCopy.arrayLoanCopiesGlobal) {
            if (lc.copyID == c.getCopyID()) {
                arrayLoanCopiesGlobal.remove(lc);
            }
            
        }
        
    }
}
