<html>
   <body>
       <div class ="header">
           <h1>Login</h1>
       </div>
      <form action = "RegistrationServlet" method = "GET">
        <label for="uname">Username:</label>
        <input type = "text" name = "username"> <br />
	<label for="pword">Password:</label>
        <input type ="text" name ="password" ><br/> <br/>
        <input type = "submit" value = "Submit" /> <br/>
        <p class="text-center" style ="color: green">${badLogin}</p>
      </form>
        <a href="http://localhost:8080/logintest/Registration.jsp">No Account? Sign up instead!</a> 
   </body>
</html>