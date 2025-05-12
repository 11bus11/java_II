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

    public static ArrayList<Copy> createCopy() {
        DataSource dataSource = DbUtil.createDataSource();
            ArrayList <Copy> arrayCopies = new ArrayList<Copy>();
            
            try (Connection connection = dataSource.getConnection()) {
                ResultSet resultSet = connection.createStatement().executeQuery("select * from Copy");
                while(resultSet.next()){
                    int copyID = resultSet.getInt("CopyID");


                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    String email = resultSet.getString("Email");
                    String password = resultSet.getString("Password");
                    String userType = resultSet.getString("UserType");
                    Copy copy = new Copy(copyID, firstName, lastName, email, password, userType);
                    arrayCopies.add(copy);
                    System.out.println(user.lastName);
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
        }

            
        return arrayCopies;
    }

    //Global variable containing all users
    static ArrayList <User> arrayUsersGlobal = createUsers();
}
