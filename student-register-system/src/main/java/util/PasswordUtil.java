package util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Hash a raw password
    public static String hashPassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    // Verify a raw password against a hashed password
    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}