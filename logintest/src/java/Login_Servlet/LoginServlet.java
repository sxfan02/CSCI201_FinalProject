
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

   private String message;

   public void init() throws ServletException {
      // Do required initialization
      message = "Yessah";
   }

   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

       String username = request.getParameter("username");
       String password = request.getParameter("password");
       String repword = request.getParameter("re_password");
       String email = request.getParameter("email");
       
      if (repword.equals(password))
      {
        System.out.println("Usename Input: " + username);
        System.out.println("Password Input: " + password);
        System.out.println("Email: " + email);
      }
      else {
          System.out.println("Password entries do not match");
      }
      
      
      // Set response content type
      response.setContentType("text/html");

      // Actual logic goes here.
      PrintWriter out = response.getWriter();
      out.println("<h1>" + message + "</h1>");
   }

   public void destroy() {
      // do nothing.
   }
}


