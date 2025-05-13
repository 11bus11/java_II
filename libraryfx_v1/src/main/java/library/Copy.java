package library;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;


public class Copy {

   //Attributes
    private int    copyID;
    private String barcode;
    private Work   work;            
    private boolean isReference;
    private String  copyStatus;
    private String  copyPlacement;


    //Constructor
    public Copy(int copyID, String barcode, Work work,
                boolean isReference, String copyStatus, String copyPlacement) {
        this.copyID        = copyID;
        this.barcode       = barcode;
        this.work          = work;
        this.isReference   = isReference;
        this.copyStatus    = copyStatus;
        this.copyPlacement = copyPlacement;
    }

    /* ---------- Getters ---------- */
    public int    getCopyID()       { return copyID; }
    public String getBarcode()      { return barcode; }
    public Work   getWork()         { return work; }
    public boolean isReference()    { return isReference; }
    public String getCopyStatus()   { return copyStatus; }
    public String getCopyPlacement(){ return copyPlacement; }

    /* ---------- Setters ---------- */
    public void setBarcode      (String v){ this.barcode       = v; }
    public void setIsReference  (boolean r){ this.isReference  = r; }
    public void setCopyPlacement(String p){ this.copyPlacement = p; }


    //Loads all copies from the database and returns them as a list
    public static ArrayList<Copy> createCopies() {
        ArrayList<Copy> list = new ArrayList<>();
        DataSource ds = DbUtil.createDataSource();
        try (Connection c = ds.getConnection();
             ResultSet rs = c.createStatement().executeQuery("SELECT * FROM Copy")) {
            while (rs.next()) {
                int workID = rs.getInt("WorkID");
                Work w = findWork(workID);
                list.add(new Copy(
                    rs.getInt   ("CopyID"),
                    rs.getString("CopyBarcode"),
                    w,
                    rs.getBoolean("IsReference"),
                    rs.getString ("CopyStatus"),
                    rs.getString ("CopyPlacement")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Finds the corresponding work from the cached global list
    private static Work findWork(int id){
        for(Work w: Work.arrayWorksGlobal)
            if(w.getWorkID()==id) return w;
        return null;
    }

    // Global list of copies loaded from the database at class initialization
    public static final ArrayList<Copy> arrayCopiesGlobal = createCopies();
}