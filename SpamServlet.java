import java.io.*;
import java.sql.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/spam")
public class SpamServlet extends HttpServlet {


protected void doPost(
HttpServletRequest request,
HttpServletResponse response)
throws IOException {

response.setContentType(
"text/html;charset=UTF-8"
);

try{

String email=
request.getParameter("email");

String message=
request.getParameter("message");

String time=
request.getParameter("time");


if(email==null || email.isBlank()
|| message==null || message.isBlank()
|| time==null || time.isBlank()){

throw new Exception(
"⚠ Fill all fields"
);

}


if(!message.contains(":")){

throw new Exception(
"⚠ Use TYPE: message format"
);

}


String type=
message.substring(
0,
message.indexOf(":")
).trim();


Connection con=
DBConnection.getConnection();


if(con==null){

throw new Exception(
"Database connection failed"
);

}


Statement st=
con.createStatement();


st.executeUpdate(
"CREATE TABLE IF NOT EXISTS email_alerts("+
"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
"email_id TEXT,"+
"alert_type TEXT,"+
"content TEXT,"+
"timestamp TEXT)"
);



PreparedStatement ps=
con.prepareStatement(
"INSERT INTO email_alerts(email_id,alert_type,content,timestamp) VALUES(?,?,?,?)"
);


ps.setString(1,email);
ps.setString(2,type);
ps.setString(3,message);
ps.setString(4,time);

ps.executeUpdate();



ResultSet rs=
st.executeQuery(
"SELECT * FROM email_alerts " +
"WHERE UPPER(alert_type)='SPAM'"
);


PrintWriter out=
response.getWriter();


StringBuffer sb=
new StringBuffer();


while(rs.next()){

sb.append(
"📩 Email: "
)
.append(
rs.getString("email_id")
)

.append(
"<br>⚠ Type: "
)
.append(
rs.getString("alert_type")
)

.append(
"<br>📝 Message: "
)
.append(
rs.getString("content")
)

.append(
"<br>⏰ Time: "
)
.append(
rs.getString("timestamp")
)

.append(
"<br>-----------------------------<br>"
);

}


out.println(
sb.toString()
);


con.close();

}


catch(SQLException e){

response.getWriter().println(
"Database Error: "+
e.getMessage()
);

}


catch(Exception e){

response.getWriter().println(
e.getMessage()
);

}

}




protected void doGet(
HttpServletRequest request,
HttpServletResponse response)
throws IOException {

response.setContentType(
"text/html;charset=UTF-8"
);

String action=
request.getParameter(
"action"
);


try{

Connection con=
DBConnection.getConnection();


if(con==null){

throw new Exception(
"Database connection failed"
);

}


String query=
"SELECT * FROM email_alerts";


if("time".equals(action))
query+=" ORDER BY timestamp";


if("type".equals(action))
query+=" ORDER BY alert_type";



Statement st=
con.createStatement();


ResultSet rs=
st.executeQuery(
query
);


PrintWriter out=
response.getWriter();


StringBuffer sb=
new StringBuffer();


while(rs.next()){

sb.append(
"📩 Email: "
)
.append(
rs.getString("email_id")
)

.append(
"<br>⚠ Type: "
)
.append(
rs.getString("alert_type")
)

.append(
"<br>📝 Message: "
)
.append(
rs.getString("content")
)

.append(
"<br>⏰ Time: "
)
.append(
rs.getString("timestamp")
)

.append(
"<br>-----------------------------<br>"
);

}


out.println(
sb.toString()
);


con.close();

}

catch(Exception e){

response.getWriter().println(
e.getMessage()
);

}

}

}