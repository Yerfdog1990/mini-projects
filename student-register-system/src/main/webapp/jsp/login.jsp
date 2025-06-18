<%--
  Created by IntelliJ IDEA.
  User: godfrey
  Date: 18/06/2025
  Time: 09:00
  Description: Login page for Student Register application
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Login - Student Register</title>
  <link rel="stylesheet" href="../css/login.css"> <!-- External CSS for styling -->
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> <!-- Load jQuery -->
</head>
<body>
<header>
  <h2>Student Register Login</h2>
</header>

<%-- Feedback or status area for login errors --%>
<div id="loginFeedback" aria-live="polite"></div>

<%-- Login form --%>
<form id="loginForm">
  <label for="email">Email:</label>
  <input type="email" id="email" name="email" placeholder="Enter your email" required>

  <label for="password">Password:</label>
  <input type="password" id="password" name="password" placeholder="Enter your password" required>

  <button type="submit">Login</button>
</form>

<footer>
  <p>&copy; Student Register App</p>
</footer>

<script>
  $(document).ready(function () {
    // Handle the login form submission
    $("#loginForm").submit(function (e) {
      e.preventDefault();

      // Collect form data
      const email = $("#email").val();
      const password = $("#password").val();

      // Send a login request to the backend
      $.post("auth", { action: "login", email: email, password: password })
              .done(function (response) {
                // Handle a successful login
                $("#loginFeedback").html('<p class="success">Login successful! Redirecting...</p>');
                setTimeout(() => {
                  window.location.href = "dashboard.jsp";
                }, 2000); // Redirect to dashboard
              })
              .fail(function () {
                // Handle a failed login
                $("#loginFeedback").html('<p class="error">Invalid login credentials. Please try again.</p>');
              });
    });
  });
</script>
</body>
</html>