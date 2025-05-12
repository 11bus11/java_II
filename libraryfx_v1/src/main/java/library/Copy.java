package library;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

public class Copy {
    private String copyID;
    private String barcode;
    private Work work;
    private boolean isReference;
    private String copyStatus;
    private String copyPlacement;

    

    

    //created copies from the database
    public static ArrayList<Copy> createCopies() {
        DataSource dataSource = DbUtil.createDataSource();
            ArrayList <Copy> arrayCopies = new ArrayList<Copy>();
            
            try (Connection connection = dataSource.getConnection()) {
                ResultSet resultSet = connection.createStatement().executeQuery("select * from Copy");
                while(resultSet.next()){
                    int copyID = resultSet.getInt("CopyID");
                    String barcode = resultSet.getString("Barcode");
                    Work work = findCopyWork(resultSet.getInt("WorkID"));
                    Boolean isReference = resultSet.getBoolean("IsReference");
                    String copyStatus = resultSet.getString("CopyStatus");
                    String copyPlacement = resultSet.getString("CopyPlacement");
                    Copy copy = new Copy(copyID, barcode, work, isReference, copyStatus, copyPlacement);
                    arrayCopies.add(copy);
                    System.out.println(copy.barcode);
                }
            } catch (SQLException e) {
                e.printStackTrace();
        }

            
        return arrayCopies;
    }

    //finding the work that the copy is connected to
    public static Work findCopyWork(int id) {
        Work copyWork = null;
        for (Work i : Work.arrayWorksGlobal) {
            if (i.workID == id) {
                copyWork = i;
            }
        }
        return copyWork;
    }

    //Global variable containing all copies
    static ArrayList <Copy> arrayCopiesGlobal = createCopies();
}
