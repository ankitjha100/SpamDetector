import java.sql.*;

public class DBConnection {

public static Connection getConnection(){

try{

Class.forName(
"org.postgresql.Driver"
);

return DriverManager.getConnection(

"jdbc:postgresql://dpg-d82o5v0g4nts73b6tc8g-a.oregon-postgres.render.com:5432/spamdb_x7m4",

"spamuser",

"fTY2RXXuvoqvRb9BeNjkxJBfI3zV3Fqp"

);

}
catch(Exception e){

e.printStackTrace();
return null;

}

}

}