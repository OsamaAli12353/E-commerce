package ecommerce.ecommerce.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service for sending emails.
 * Handles OTP email delivery during user registration.
 */
@Service
public class EmailService {

    // Spring's email sender component
    private final JavaMailSender mailSender;

    // Constructor
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends OTP code to user's email.
     *
     * @param to Recipient's email address
     * @param otp The OTP code to send
     */
    public void sendOtp(String to, String otp) {
        // Create email message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp);

        // Send email
        mailSender.send(message);
    }
}