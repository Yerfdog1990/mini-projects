package model;


import javax.persistence.*;


public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    // Parameterized constructor
    public AdminEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Non-parameterized constructor
    public AdminEntity() {}

    // Getters
    public Long getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
