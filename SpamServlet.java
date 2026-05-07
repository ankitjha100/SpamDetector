import java.io.*;
import java.sql.*;
import java.util.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;


/* -------- EMAIL ALERT CLASS -------- */

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



/* -------- ALERT TYPE COMPARATOR -------- */

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



/* -------- TIME COMPARATOR -------- */

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


ArrayList<EmailAlert> alerts=
new ArrayList<>();



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



/* -------- EXCEPTION HANDLING -------- */

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



/* -------- GET ALERT TYPE -------- */

String type=
message.substring(
0,
message.indexOf(":")
).trim();



/* -------- STORE IN ARRAYLIST -------- */

EmailAlert alert=
new EmailAlert(
email,
type,
message,
time
);

alerts.add(alert);



/* -------- SQLITE CONNECTION -------- */

Connection con=
DBConnection.getConnection();


if(con==null){

throw new Exception(
"Database connection failed"
);

}



/* -------- CREATE TABLE -------- */

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



/* -------- INSERT INTO DB -------- */

PreparedStatement ps=
con.prepareStatement(

"INSERT INTO email_alerts(email_id,alert_type,content,timestamp) VALUES(?,?,?,?)"

);

ps.setString(1,email);
ps.setString(2,type);
ps.setString(3,message);
ps.setString(4,time);

ps.executeUpdate();



/* -------- SHOW ONLY SPAM ALERTS -------- */

PrintWriter out=
response.getWriter();

StringBuffer sb=
new StringBuffer();

for(EmailAlert a : alerts){

if(a.alertType.equalsIgnoreCase(
"SPAM"
)){

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

}

out.println(
sb.toString()
);

con.close();

}


catch(SQLException e){

response.getWriter().println(

"Database Error: "
+e.getMessage()

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


/* -------- SORTING -------- */

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



/* -------- DISPLAY ALL -------- */

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

}


catch(Exception e){

response.getWriter().println(
e.getMessage()
);

}

}

}