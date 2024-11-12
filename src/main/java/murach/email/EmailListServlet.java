package murach.email;

import java.io.*;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import murach.business.User;
import murach.data.UserDB;

public class EmailListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/index.html";
        String message = "";

        // Get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "join"; // Default action
        }

        // Perform action and set URL to appropriate page
        if (action.equals("join")) {
            url = "/index.jsp"; // The "join" page
        } else if (action.equals("add")) {
            // Get parameters from the request
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            // Store data in User object
            User user = new User(firstName, lastName, email);
            System.out.println("Attempting to add user: " + user.getEmail());

            // Validate the parameters
            if (UserDB.emailExists(user.getEmail())) {
                message = "This email address already exists.<br>Please enter another email address.";
                url = "/index.jsp";
            } else {
                message = "";
                url = "/thanks.jsp";
                UserDB.insert(user);
            }

            // Set user and message attributes
            request.setAttribute("user", user);
            request.setAttribute("message", message);
        }

        // Forward request and response to the specified URL
        getServletContext().getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
