package model;


import util.PasswordUtil;

import javax.persistence.*;

@Entity
@Table(name = "student_entity")

public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "classroom", nullable = false)
    private String classroom;

    @Column(name = "password", nullable = false)
    private String password; // Password should no longer be unique; this avoids potential conflicts for users with the same hashed password.

    // Parameterized constructor
    public StudentEntity(String name, String email, String classroom, String password) {
        this.name = name;
        this.email = email;
        this.classroom = classroom;
        this.password = password;
    }

    // Non-parameterized constructor
    public StudentEntity() {}

    // Utility function to set a hashed password
    public void setHashedPassword(String rawPassword) {
        this.password = PasswordUtil.hashPassword(rawPassword); // Use the utility class for hashing.
    }

    // Getters
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getClassroom() {
        return classroom;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}