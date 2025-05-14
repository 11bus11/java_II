package library;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

/** Represents a physical copy of a Work. */
public class Copy {

    /* ---------- attributes ---------- */
    private int    copyID;
    private String barcode;
    private Work   work;
    private boolean isReference;
    private String  copyStatus;      // "available" or "loaned"
    private String  copyPlacement;

    /* ---------- constructor ---------- */
    public Copy(int copyID, String barcode, Work work,
                boolean isReference, String copyStatus, String copyPlacement) {

        this.copyID        = copyID;
        this.barcode       = barcode;
        this.work          = work;
        this.isReference   = isReference;
        this.copyStatus    = copyStatus;
        this.copyPlacement = copyPlacement;
    }

    /* ---------- getters ---------- */
    public int     getCopyID()       { return copyID;        }
    public String  getBarcode()      { return barcode;       }
    public Work    getWork()         { return work;          }
    public boolean isReference()     { return isReference;   }
    public String  getCopyStatus()   { return copyStatus;    }
    public String  getCopyPlacement(){ return copyPlacement; }

    /* ---------- setters ---------- */
    public void setBarcode      (String v){ this.barcode       = v; }
    public void setIsReference  (boolean v){ this.isReference  = v; }
    public void setCopyPlacement(String v){ this.copyPlacement = v; }

    /** NEW setter - used by LoanController to change status. */
    public void setCopyStatus(String status){
        this.copyStatus = status;
    }

    /* ---------- initial load ---------- */
    public static ArrayList<Copy> createCopies() {
        ArrayList<Copy> list = new ArrayList<>();
        DataSource ds = DbUtil.createDataSource();
        try (Connection c = ds.getConnection();
             ResultSet rs = c.createStatement().executeQuery("SELECT * FROM Copy")) {

            while (rs.next()) {
                int  workID = rs.getInt("WorkID");
                Work w = findWork(workID);
                list.add(new Copy(
                    rs.getInt   ("CopyID"),
                    rs.getString("CopyBarcode"),
                    w,
                    rs.getBoolean("IsReference"),
                    rs.getString ("CopyStatus"),
                    rs.getString ("CopyPlacement")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private static Work findWork(int id){
        for (Work w : Work.arrayWorksGlobal)
            if (w.getWorkID() == id) return w;
        return null;
    }

    /** Global cache of copies. */
    public static final ArrayList<Copy> arrayCopiesGlobal = createCopies();
}
