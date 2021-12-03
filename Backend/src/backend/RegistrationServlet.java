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
       
        
        if(!password.equals("") && !username.equals("") && !repword.equals(""))
        {
           if (username.matches("[a-zA-Z]+"))
           {
               if (password.matches("[a-zA-Z]+"))
               {
                   if (password.equals(repword))
                   {
                        if (Login.onRegistration("users", username))
                        {
                            String hashed = Login.Hash(password);
                            System.out.println(hashed);
                            Login.post("users", username, hashed, email);
                            pw.println("<h1>" + "Thank You For Registering" + "</h1>");
                        }
                        else {
                            request.setAttribute("sameUsername", "Username already exists");
                            request.getRequestDispatcher("Registration.jsp").forward(request, response); 
                        }
                   }
                   else {
                       request.setAttribute("passmismatch", "Password entries do not match");
                       request.getRequestDispatcher("Registration.jsp").forward(request, response); 
                   }
               }
               else 
               {
                  request.setAttribute("formatP", "Password must use only alphabetical characters");
                  request.getRequestDispatcher("Registration.jsp").forward(request, response);
               }
           }
           else {
               request.setAttribute("formatU", "Username must use only alphabetical characters");
               request.getRequestDispatcher("Registration.jsp").forward(request, response);
           }
        }
        else {
           request.setAttribute("none", "Please fill out all fields");
           request.getRequestDispatcher("Registration.jsp").forward(request, response);
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
       else{
           request.setAttribute("badLogin", "Incorrect login information");
           request.getRequestDispatcher("Login.jsp").forward(request, response);
       }
   }

   public void destroy() {
      // do nothing.
   }
}


