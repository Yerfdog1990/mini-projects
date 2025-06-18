<%--
  Created by IntelliJ IDEA.
  User: godfrey
  Date: 17/06/2025
  Time: 14:24
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.*, java.util.*" %>
<%@ page import="controller.AuthServlet" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Student Management Dashboard</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        // Fetch all students and populate the table
        function fetchStudents() {
            $.ajax({
                url: "student",
                method: "POST",
                success: function(data) {
                    let tableBody = $("#studentTable tbody");
                    tableBody.empty(); // Clear any existing rows
                    data.forEach(student => {
                        tableBody.append(`
                            <tr>
                                <td>${student.id}</td>
                                <td>${student.name}</td>
                                <td>${student.email}</td>
                                <td>${student.classroom}</td>
                                <td>
                                    <button onclick="editStudent(${student.id}, '${student.name}', '${student.email}', '${student.classroom}')">Edit</button>
                                    <button onclick="deleteStudent(${student.id})">Delete</button>
                                </td>
                            </tr>
                        `);
                    });
                },
                error: function(err) {
                    alert("Failed to fetch students.");
                }
            });
        }

        // Add a new student
        function addStudent() {
            const name = $("#name").val();
            const email = $("#email").val();
            const classroom = $("#classroom").val();
            const password = $("#password").val();
            $.ajax({
                url: "student",
                method: "GET",
                data: { action: "add", name, email, classroom, password },
                success: function() {
                    fetchStudents();
                    alert("Student added successfully.");
                    $("#addStudentForm")[0].reset();
                },
                error: function() {
                    alert("Failed to add student.");
                }
            });
        }

        // Edit an existing student
        function editStudent(id, name, email, classroom) {
            $("#editId").val(id);
            $("#editName").val(name);
            $("#editEmail").val(email);
            $("#editClassroom").val(classroom);
            $("#editModal").show();
        }

        function saveEdit() {
            const id = $("#editId").val();
            const name = $("#editName").val();
            const email = $("#editEmail").val();
            const classroom = $("#editClassroom").val();
            const password = $("#editPassword").val(); // Optional field
            $.ajax({
                url: "student",
                method: "GET",
                data: { action: "edit", id, name, email, classroom, password },
                success: function() {
                    fetchStudents();
                    alert("Student updated successfully.");
                    $("#editModal").hide();
                },
                error: function() {
                    alert("Failed to update student.");
                }
            });
        }

        // Delete a student
        function deleteStudent(id) {
            if (confirm("Are you sure you want to delete this student?")) {
                $.ajax({
                    url: "student",
                    method: "GET",
                    data: { action: "delete", id },
                    success: function() {
                        fetchStudents();
                        alert("Student deleted successfully.");
                    },
                    error: function() {
                        alert("Failed to delete student.");
                    }
                });
            }
        }

        // Hide edit modal on cancel
        function cancelEdit() {
            $("#editModal").hide();
        }

        // Fetch students on page load
        $(document).ready(function() {
            fetchStudents();
        });
    </script>
</head>
<body>
<%-- Authentication Check --%>
<%
    if (!AuthServlet.isAuthenticated((HttpServletRequest) request)) {
        response.sendRedirect("login.jsp?error=Please log in to access the dashboard.");
        return;
    }
%>

<h1>Student Management Dashboard</h1>

<%-- Add Student Form --%>
<h2>Add Student</h2>
<form id="addStudentForm" onsubmit="event.preventDefault(); addStudent();">
    <label for="name">Name:</label>
    <input type="text" id="name" name="name" required>
    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required>
    <label for="classroom">Classroom:</label>
    <input type="text" id="classroom" name="classroom" required>
    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required>
    <button type="submit">Add Student</button>
</form>

<%-- Student Table --%>
<h2>Student List</h2>
<table id="studentTable" border="1">
    <thead>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Email</th>
        <th>Classroom</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <%-- Rows will be populated via AJAX --%>
    </tbody>
</table>

<%-- Edit Student Modal --%>
<div id="editModal" style="display:none;">
    <h2>Edit Student</h2>
    <form onsubmit="event.preventDefault(); saveEdit();">
        <input type="hidden" id="editId">
        <label for="editName">Name:</label>
        <input type="text" id="editName" required>
        <label for="editEmail">Email:</label>
        <input type="email" id="editEmail" required>
        <label for="editClassroom">Classroom:</label>
        <input type="text" id="editClassroom" required>
        <label for="editPassword">Password:</label>
        <input type="password" id="editPassword">
        <button type="submit">Save Changes</button>
        <button type="button" onclick="cancelEdit();">Cancel</button>
    </form>
</div>
</body>
</html>