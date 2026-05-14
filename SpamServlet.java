import java.io.*;
import java.sql.*;
import java.util.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;



/* ---------- EMAIL ALERT ---------- */

class EmailAlert {

String emailId;
String alertType;
String content;
String timestamp;

EmailAlert(
String emailId,
String alertType,
String content,
String timestamp
){

this.emailId=emailId;
this.alertType=alertType;
this.content=content;
this.timestamp=timestamp;

}

}



/* ---------- ALERT TYPE COMPARATOR ---------- */

class AlertTypeComparator
implements Comparator<EmailAlert>{

@Override
public int compare(
EmailAlert a,
EmailAlert b
){

return a.alertType.compareToIgnoreCase(
b.alertType
);

}

}



/* ---------- TIME COMPARATOR ---------- */

class TimeComparator
implements Comparator<EmailAlert>{

@Override
public int compare(
EmailAlert a,
EmailAlert b
){

return a.timestamp.compareTo(
b.timestamp
);

}

}





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
request.getParameter(
"email"
);

String message=
request.getParameter(
"message"
);

String time=
request.getParameter(
"time"
);



/* ---------- EXCEPTION HANDLING ---------- */

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



/* ---------- GET TYPE ---------- */

String type=
message.substring(
0,
message.indexOf(":")
).trim();




/* ---------- DB CONNECTION ---------- */

Connection con=
DBConnection.getConnection();

if(con==null){

throw new Exception(
"Database connection failed"
);

}




/* ---------- INSERT INTO POSTGRES ---------- */

PreparedStatement ps=
con.prepareStatement(

"INSERT INTO email_alerts(email_id,alert_type,content,timestamp) VALUES(?,?,?,?)"

);

ps.setString(1,email);
ps.setString(2,type);
ps.setString(3,message);
ps.setString(4,time);

ps.executeUpdate();




/* ---------- FETCH ONLY SPAM ALERTS ---------- */

PreparedStatement spamPs=
con.prepareStatement(

"SELECT * FROM email_alerts WHERE UPPER(alert_type)='SPAM'"

);

ResultSet rs=
spamPs.executeQuery();

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



/* ---------- FETCH FROM DATABASE ---------- */

String query=
"SELECT * FROM email_alerts";


PreparedStatement ps=
con.prepareStatement(
query
);

ResultSet rs=
ps.executeQuery();



/* ---------- STORE INTO ARRAYLIST ---------- */

ArrayList<EmailAlert> alerts=
new ArrayList<>();


while(rs.next()){

alerts.add(

new EmailAlert(

rs.getString("email_id"),

rs.getString("alert_type"),

rs.getString("content"),

rs.getString("timestamp")

)

);

}




/* ---------- SORTING ---------- */

if("time".equals(action)){

Collections.sort(
alerts,
new TimeComparator()
);

}


if("type".equals(action)){

Collections.sort(
alerts,
new AlertTypeComparator()
);

}



/* ---------- DISPLAY ---------- */

PrintWriter out=
response.getWriter();

StringBuffer sb=
new StringBuffer();


for(EmailAlert a : alerts){

sb.append(
"📩 Email: "
).append(a.emailId)

.append(
"<br>⚠ Type: "
).append(a.alertType)

.append(
"<br>📝 Message: "
).append(a.content)

.append(
"<br>⏰ Time: "
).append(a.timestamp)

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