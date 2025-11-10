package ecommerce.ecommerce.service;

public interface OTPService {
    String generateOtp(String email);
    boolean verifyOtp(String email, String otp);

}
