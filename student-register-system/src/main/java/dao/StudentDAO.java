package dao;

import model.StudentEntity;

import java.util.List;
import java.util.Optional;

import static util.HibernateUtil.doWithSession;

// Data access layer (DAO)
public class StudentDAO {

    // Save student to register with hashed password
    public void register(StudentEntity student) {
        validateStudentEntity(student); // Validate the student before saving.
        student.setHashedPassword(student.getPassword()); // Hash password before saving.
        doWithSession(session -> session.save(student));
    }

    // Retrieve a student by ID
    public Optional<StudentEntity> getById(int id) {
        StudentEntity student = doWithSession(session -> session.get(StudentEntity.class, id));
        return Optional.ofNullable(student); // Prevents NullPointerException.
    }

    // Retrieve all students
    public List<StudentEntity> getAll() {
        List<StudentEntity> list = doWithSession(session -> session.createQuery("FROM StudentEntity").getResultList());
        return list;
    }

    // Edit student details in the register
    public void update(StudentEntity student) {
        validateStudentEntity(student); // Validate before updating.
        // Hash the password only if it is being updated (non-null).
        if (student.getPassword() != null) {
            student.setHashedPassword(student.getPassword());
        }
        doWithSession(session -> session.merge(student));
    }

    // Validate student entity
    private void validateStudentEntity(StudentEntity student) {
        if (student.getName() == null || student.getName().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be null or empty.");
        }
        if (student.getEmail() == null || student.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Student email cannot be null or empty.");
        }
        if (student.getClassroom() == null || student.getClassroom().isEmpty()) {
            throw new IllegalArgumentException("Student classroom cannot be null or empty.");
        }
        if (student.getPassword() == null || student.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Student password cannot be null or empty.");
        }
        if (student.getPassword().length() < 8) {
            throw new IllegalArgumentException("Student password must be at least 8 characters long.");
        }
        // Password pattern: Requires uppercase, lowercase, number, and special character.
        if (!student.getPassword().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*.,?-])[A-Za-z\\d!@#$%^&*.,?-]{8,}$")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.");
        }
    }

    // Delete a student by ID
    public void delete(Long id) {
        doWithSession(session -> session.createQuery("DELETE FROM StudentEntity WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate());
    }
}