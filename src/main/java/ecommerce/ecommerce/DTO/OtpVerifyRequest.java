package ecommerce.ecommerce.DTO;

/**
 * Data Transfer Object for OTP verification requests.
 * Used to verify the One-Time Password sent to user's email during registration.
 */
public class OtpVerifyRequest {
    // User's email address
    private String email;

    // One-Time Password sent to the user
    private String otp;

    /**
     * Constructor to create OTP verification request.
     *
     * @param email The user's email address
     * @param otp The OTP code to verify
     */
    public OtpVerifyRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    /**
     * Gets the email address.
     *
     * @return The user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email The user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the OTP code.
     *
     * @return The OTP to verify
     */
    public String getOtp() {
        return otp;
    }

    /**
     * Sets the OTP code.
     *
     * @param otp The OTP to verify
     */
    public void setOtp(String otp) {
        this.otp = otp;
    }
}