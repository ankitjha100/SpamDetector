import java.sql.*;

public class DBConnection {

public static Connection getConnection(){

try{

Class.forName("org.sqlite.JDBC");

return DriverManager.getConnection(
"jdbc:sqlite:spamdb.db"
);

}catch(Exception e){
e.printStackTrace();
return null;
}

}

}