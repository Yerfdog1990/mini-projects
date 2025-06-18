package controller;

import dao.StudentDAO;
import model.StudentEntity;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/student")
public class StudentServlet extends HttpServlet {
    private final StudentDAO dao = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Check if the user is authenticated
        if (!AuthServlet.isAuthenticated(request)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is not authenticated.");
            return;
        }

        String action = request.getParameter("action");

        switch (action) {
            case "add":
                StudentEntity student = new StudentEntity();
                student.setName(request.getParameter("name"));
                student.setEmail(request.getParameter("email"));
                student.setClassroom(request.getParameter("classroom"));
                student.setPassword(request.getParameter("password"));
                dao.register(student);
                break;
            case "edit":
                int id = Integer.parseInt(request.getParameter("id"));
                Optional<StudentEntity> editStudent = dao.getById(id);
                editStudent.ifPresent(s -> {
                    s.setName(request.getParameter("name"));
                    s.setEmail(request.getParameter("email"));
                    s.setClassroom(request.getParameter("classroom"));
                    s.setPassword(request.getParameter("password"));
                    dao.update(s);
                });
                break;
            case "delete":
                dao.delete(Long.parseLong(request.getParameter("id")));
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
                return;
        }
        response.setContentType("application/json");
        response.getWriter().write("{\"status\":\"Ok\"}");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Check if the user is authenticated
        if (!AuthServlet.isAuthenticated(request)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is not authenticated.");
            return;
        }

        List<StudentEntity> students = dao.getAll();
        StringBuilder json = new StringBuilder("[");
        for (StudentEntity student : students) {
            json.append(String.format(
                    "{\"id\":\"%d\", \"name\":\"%s\", \"email\":\"%s\", \"classroom\":\"%s\"},",
                    student.getId(), student.getName(), student.getEmail(), student.getClassroom()));
        }
        if (!students.isEmpty()) json.setLength(json.length() - 1);
        json.append("]");
        response.setContentType("application/json");
        response.getWriter().write(json.toString());
    }
}