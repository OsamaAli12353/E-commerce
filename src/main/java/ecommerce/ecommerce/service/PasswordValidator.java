package ecommerce.ecommerce.service;

public class PasswordValidator {

    public static boolean isValid(String password) {
        String regex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,32}$";
        return password.matches(regex);

    }
}

