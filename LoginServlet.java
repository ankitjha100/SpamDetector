import java.io.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

protected void doPost(
HttpServletRequest request,
HttpServletResponse response)
throws IOException {

response.setContentType(
"text/html"
);

try{

String user=
request.getParameter(
"username"
);

String pass=
request.getParameter(
"password"
);


if(user==null || user.isBlank()
|| pass==null || pass.isBlank()){

throw new Exception(
"Fill all login fields"
);

}


if(
"admin".equals(user)
&&
"1234".equals(pass)
){

response.sendRedirect(
request.getContextPath()
+"/index.html"
);

}
else{

throw new Exception(
"Invalid username or password"
);

}

}

catch(Exception e){

response.getWriter().println(

"<h2>"
+e.getMessage()+
"</h2>"

);

}

}

}