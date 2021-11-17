<%-- 
    Document   : Login.jsp
    Created on : Nov 16, 2021, 4:49:02 PM
    Author     : iggym
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
   <body>
       <div class ="header">
           <h1>Sign Up</h1>
       </div>
      <form action = "RegistrationServlet" method = "POST">
        <label for="uname">Username:</label>
        <input type = "text" name = "username"> <b>${formatU}</b>
        <br>
        <label for="pword">Password:</label>
        <input type = "text" name = "password" > <b>${formatP}</b>
        <br />
        <label for="repword">Re-enter Password: </label>
        <input type ="text" name ="re_password" > <b>${passmismatch}</b>
        </br>
        <label for="email">Email:</label>
        <input type="text" name ="email"> <b>${formatE}</b>
        <br/> <br/>
        <input type = "submit" value = "Submit" />
        <p class="text-center" style="color: green">${none}</p>
      </form>
   </body>
</html>

