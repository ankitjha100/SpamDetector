import java.sql.*;

public class DBConnection {

public static Connection getConnection(){

try{

Class.forName(
"org.postgresql.Driver"
);

return DriverManager.getConnection(

"jdbc:postgresql://localhost:5432/spamdb",

"postgres",

"Ankit"

);

}
catch(Exception e){

e.printStackTrace();
return null;

}

}

}