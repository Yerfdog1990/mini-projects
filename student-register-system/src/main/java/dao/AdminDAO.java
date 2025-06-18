package dao;

import model.AdminEntity;
import util.HibernateUtil;
import util.PasswordUtil;

import java.util.Optional;
import java.util.List;

// Data Access Layer
public class AdminDAO {

    // Register a new admin with a hashed password
    public void register(AdminEntity admin) {
        validateAdminEntity(admin); // Validate admin details
        admin.setPassword(PasswordUtil.hashPassword(admin.getPassword())); // Hash the password
        HibernateUtil.doWithSession(session -> session.save(admin));
    }

    // Retrieve an admin by email
    public Optional<AdminEntity> getByEmail(String email) {
        return HibernateUtil.doWithSession(session -> {
            AdminEntity admin = session.createQuery("FROM AdminEntity WHERE email = :email", AdminEntity.class)
                    .setParameter("email", email)
                    .uniqueResult();
            return Optional.ofNullable(admin); // Prevents NullPointerException
        });
    }

    // Validate Admin Login (email + password)
    public boolean validateLogin(String email, String rawPassword) {
        Optional<AdminEntity> admin = getByEmail(email);

        // Check if the email exists and verify the raw password against the hashed one
        return admin.isPresent() && PasswordUtil.verifyPassword(rawPassword, admin.get().getPassword());
    }

    // Retrieve an admin by ID
    public Optional<AdminEntity> getById(Long id) {
        return HibernateUtil.doWithSession(session -> {
            AdminEntity admin = session.get(AdminEntity.class, id);
            return Optional.ofNullable(admin); // Prevents NullPointerException
        });
    }

    // Retrieve all admins
    public List<AdminEntity> getAll() {
        return HibernateUtil.doWithSession(session ->
                session.createQuery("FROM AdminEntity", AdminEntity.class).getResultList());
    }

    // Update an admin (e.g., reset password)
    public void update(AdminEntity admin) {
        validateAdminEntity(admin); // Validate admin details

        // Only hash the password if it is being updated (non-null and not empty)
        if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
            admin.setPassword(PasswordUtil.hashPassword(admin.getPassword()));
        }

        HibernateUtil.doWithSession(session -> {
            session.update(admin);
            return null;
        });
    }

    // Delete an admin by ID
    public void delete(Long id) {
        HibernateUtil.doWithSession(session -> {
            session.createQuery("DELETE FROM AdminEntity WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            return null;
        });
    }

    // Input validation for the admin entity
    private void validateAdminEntity(AdminEntity admin) {
        if (admin.getEmail() == null || admin.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Admin email cannot be null or empty.");
        }
        if (admin.getPassword() == null || admin.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Admin password cannot be null or empty.");
        }
        if (admin.getPassword().length() < 6) {
            throw new IllegalArgumentException("Admin password must be at least 6 characters long.");
        }
        // Password pattern: Requires uppercase, lowercase, number, and special character.
        if (!admin.getPassword().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*.,?-])[A-Za-z\\d!@#$%^&*.,?-]{8,}$")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.");
        }
    }
}