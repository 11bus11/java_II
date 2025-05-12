package library;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

public class Copy {
    private String copyID;
    private String barcode;
    private final Work work;
    private boolean isReference;
    private String copyStatus;
    private String copyPlacement;

    public Copy(String copyID, String barcode, Work work,
                boolean isReference, String copyStatus, String copyPlacement) {
        this.copyID        = copyID;
        this.barcode       = barcode;
        this.work          = work;
        this.isReference   = isReference;
        this.copyStatus    = copyStatus;
        this.copyPlacement = copyPlacement;
    }

    public String getBarcode()   { return barcode; }
    public String getCopyStatus(){ return copyStatus; }
    public Work   getWork()      { return work; }

    public static ArrayList<Copy> createCopies() {
        DataSource dataSource = DbUtil.createDataSource();
            ArrayList <Copy> arrayCopies = new ArrayList<Copy>();
            
            try (Connection connection = dataSource.getConnection()) {
                ResultSet resultSet = connection.createStatement().executeQuery("select * from Copy");
                while(resultSet.next()){
                    int copyID = resultSet.getInt("CopyID");
                    String barcode = resultSet.getString("Barcode");
                    Work work = findCopyWork(resultSet.getString("WorkID"));
                    Boolean isReference = resultSet.getBoolean("IsReference");
                    String copyStatus = resultSet.getString("CopyStatus");
                    String copyPlacement = resultSet.getString("CopyPlacement");
                    Copy copy = new Copy(copyID, barcode, work, isReference, copyStatus, copyPlacement);
                    arrayCopies.add(copy);
                    System.out.println(user.lastName);
                }

                private String barcode;
    private final Work work;
    private boolean isReference;
    private String copyStatus;
    private String copyPlacement;
                
            } catch (SQLException e) {
                e.printStackTrace();
        }

            
        return arrayCopies;
    }

    //finding the user who made the loan
    public static Work findCopyWork(int id) {
        Work copyWork = null;
        for (Work i : Work.arrayWorksGlobal) {
            if (i.workID == id) {
                copyWork = i;
            }
        }
        return copyWork;
    }

    //Global variable containing all users
    static ArrayList <Copy> arrayCopiesGlobal = createCopies();
}
