package ecommerce.ecommerce.service;

/**
 * Utility class for password validation.
 * Ensures passwords meet security requirements.
 */
public class PasswordValidator {

    /**
     * Validates password strength based on security requirements.
     * Requirements:
     * - 8-32 characters long
     * - At least one digit (0-9)
     * - At least one uppercase letter (A-Z)
     * - At least one special character (@#$%^&+=)
     *
     * @param password The password to validate
     * @return true if password meets all requirements, false otherwise
     */
    public static boolean isValid(String password) {
        String regex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,32}$";
        return password.matches(regex);
    }
}