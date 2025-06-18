package controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Clear the auth cookie
        Cookie authCookie = new Cookie("auth_token", null);
        authCookie.setMaxAge(0); // Delete the cookie
        authCookie.setPath("/"); // Ensure it deletes it from all paths
        response.addCookie(authCookie);

        // Redirect to login page
        response.sendRedirect("login.jsp?message=Logged%20out%20successfully");
    }
}
