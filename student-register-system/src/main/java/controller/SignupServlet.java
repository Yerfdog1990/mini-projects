package controller;

import dao.AdminDAO;
import jakarta.servlet.annotation.WebServlet;
import model.AdminEntity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    // Constants for cookie and session handling
    private static final String AUTH_COOKIE_NAME = "auth_token";

    private final AdminDAO adminDAO = new AdminDAO(); // DAO for accessing admin data

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Retrieve and validate input parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            response.sendRedirect("signup.jsp?error=Email%20and%20password%20cannot%20be%20empty");
            return;
        }

        try {
            // Check if the admin account already exists
            if (adminDAO.getByEmail(email).isPresent()) {
                response.sendRedirect("signup.jsp?error=Email%20already%20exists");
                return;
            }

            // Create and register the new admin
            AdminEntity admin = new AdminEntity(email, password);
            adminDAO.register(admin);

            // Generate an authentication token
            String authToken = java.util.UUID.randomUUID().toString();

            // Store the token in a session
            HttpSession session = request.getSession(true);
            session.setAttribute("user", authToken);

            // Set a secure cookie with the authentication token
            Cookie authCookie = new Cookie(AUTH_COOKIE_NAME, authToken);
            authCookie.setHttpOnly(true); // Prevent JavaScript access
            authCookie.setSecure(true);  // Enforce HTTPS only (useful in production)
            authCookie.setMaxAge(60 * 30); // Set cookie expiry (e.g., 30 minutes)
            response.addCookie(authCookie);

            // Redirect to the login page after successful signup
            response.sendRedirect("login.jsp?success=Signup%20successful!%20Please%20login.");
        } catch (IllegalArgumentException ex) {
            // Handle validation errors
            response.sendRedirect("signup.jsp?error=" + ex.getMessage().replace(" ", "%20"));
        } catch (Exception ex) {
            // Handle unexpected errors
            response.sendRedirect("signup.jsp?error=An%20unexpected%20error%20occurred");
        }
    }
}