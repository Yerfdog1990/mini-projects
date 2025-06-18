package controller;

import util.HibernateUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    private static final String AUTH_SESSION_KEY = "user";
    private static final String AUTH_COOKIE_NAME = "auth_token";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validate admin credentials
        boolean validUser = HibernateUtil.doWithSession(session ->
                session.createQuery("select count(a) from AdminEntity a where a.email = :email and a.password = :password", Long.class)
                        .setParameter("email", email)
                        .setParameter("password", password) // Ideally hash passwords
                        .uniqueResult() > 0);

        if (validUser) {
            // Generate authentication token (e.g., UUID or secure random string)
            String authToken = java.util.UUID.randomUUID().toString();

            // Store the token in the session
            request.getSession().setAttribute(AUTH_SESSION_KEY, authToken);

            // Set the token as a secure HTTP cookie
            Cookie authCookie = new Cookie(AUTH_COOKIE_NAME, authToken);
            authCookie.setHttpOnly(true); // Prevent JavaScript access
            authCookie.setSecure(true); // Enforce HTTPS only (applicable in production)
            authCookie.setMaxAge(60 * 30); // Set cookie expiry (30 minutes)
            response.addCookie(authCookie);

            // Redirect to dashboard after successful login
            response.sendRedirect("dashboard.jsp");
        } else {
            response.sendRedirect("login.jsp?error=Invalid%20Credentials");
        }
    }

    public static boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Cookie[] cookies = request.getCookies();

        if (session != null && cookies != null) {
            String sessionToken = (String) session.getAttribute(AUTH_SESSION_KEY);
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("auth_token") && cookie.getValue().equals(sessionToken)) {
                    return true;
                }
            }
        }
        return false;
    }
}