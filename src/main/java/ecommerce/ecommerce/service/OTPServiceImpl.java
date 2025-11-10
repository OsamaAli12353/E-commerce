package ecommerce.ecommerce.service;

import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OTPServiceImpl implements OTPService {

    private final ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    @Override
    public String generateOtp(String email) {
        String otp = String.valueOf(random.nextInt(900000) + 100000); // 6 digits
        otpStorage.put(email, otp);
        System.out.println("Generated OTP for " + email + ": " + otp);
        return otp;
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        String storedOtp = otpStorage.get(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStorage.remove(email); // OTP used once
            return true;
        }
        return false;
    }
}
