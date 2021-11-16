package LoginPackage;


import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {

   public void init() throws ServletException {
      // Do required initialization
      
   }
   
   public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

       String username = request.getParameter("username");
       String password = request.getParameter("password");
       String repword = request.getParameter("re_password");
       String email = request.getParameter("email");
      
      response.setContentType("text/html");
      PrintWriter pw = response.getWriter();
       
      if (repword.equals(password))
      {
        System.out.println("Usename Input: " + username);
        System.out.println("Password Input: " + password);
        System.out.println("Email: " + email);
        if (Login.onRegistration("users", username))
        {
            Login.post("users", username, password, email);
            pw.println("<h1>" + "Thank You For Registering" + "</h1>");
        }
        else {
            pw.println("<h1>Username Already Exists</h1>");
        }
      }
      else {
          pw.println("<h1>Password entries don't match</h1>");
      }
      
      
   }
   
   public void doGet(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
   
       String username = request.getParameter("username");
       String password = request.getParameter("password");
      
       response.setContentType("text/html");
       PrintWriter pw = response.getWriter();
               
       boolean imIn = Login.onLogin("users", username, password);
       if (imIn == true) pw.println("<h1>Successful Login</h1>");
       else pw.println("<h1>Incorrect Login Credentials</h1>");
   }

   public void destroy() {
      // do nothing.
   }
}


