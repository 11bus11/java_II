package library;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

public class Copy {
    private int copyID;
    private String barcode;
    private Work work;
    private boolean isReference;
    private String copyStatus;
    private String copyPlacement;

    public Copy(int copyID, String barcode, Work work,
                boolean isReference, String copyStatus, String copyPlacement) {
        this.copyID        = copyID;
        this.barcode       = barcode;
        this.work          = work;
        this.isReference   = isReference;
        this.copyStatus    = copyStatus;
        this.copyPlacement = copyPlacement;
    }

    public static int getCopyID(Copy copy)       { return copy.copyID; }
    
    //created copies from the database
    public static ArrayList<Copy> createCopies() {
        DataSource dataSource = DbUtil.createDataSource();
            ArrayList <Copy> arrayCopies = new ArrayList<Copy>();
            
            try (Connection connection = dataSource.getConnection()) {
                ResultSet resultSet = connection.createStatement().executeQuery("select * from Copy");
                while(resultSet.next()){
                    int copyID = resultSet.getInt("CopyID");
                    String barcode = resultSet.getString("CopyBarcode");
                    Work work = findCopyWork(resultSet.getInt("WorkID"));
                    System.out.println(work);
                    Boolean isReference = resultSet.getBoolean("IsReference");
                    String copyStatus = resultSet.getString("CopyStatus");
                    String copyPlacement = resultSet.getString("CopyPlacement");
                    Copy copy = new Copy(copyID, barcode, work, isReference, copyStatus, copyPlacement);
                    arrayCopies.add(copy);
                    System.out.println(copy.barcode);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error copy");
        }

            
        return arrayCopies;
    }

    //finding the work that the copy is connected to
    public static Work findCopyWork(int id) {
        int index = 0;
        Work copyWork = null;
        while (Work.arrayWorksGlobal.size() > index) {
            if (Work.getWorkID(Work.arrayWorksGlobal.get(index)) == id) {
                copyWork = Work.arrayWorksGlobal.get(index);
            }
            System.out.println(Work.arrayWorksGlobal.get(index));
            index++;
        }
        return copyWork;
    }

    //Global variable containing all copies
    static ArrayList <Copy> arrayCopiesGlobal = createCopies();
}
    

