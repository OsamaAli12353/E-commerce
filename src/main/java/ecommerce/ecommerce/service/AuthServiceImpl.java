package ecommerce.ecommerce.service;

import ecommerce.ecommerce.DTO.OtpVerifyRequest;
import ecommerce.ecommerce.DTO.PendingUser;
import ecommerce.ecommerce.DTO.UserDTO;
import ecommerce.ecommerce.entity.User;
import ecommerce.ecommerce.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;

/**
 * Service implementation for authentication operations.
 * Handles user registration with OTP verification using Redis for temporary storage.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor with dependency injection.
     * Configures Redis serializers for proper data storage.
     */
    public AuthServiceImpl(UserRepository userRepository, EmailService emailService,
                           RedisTemplate<String, Object> redisTemplate,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.redisTemplate = redisTemplate;
        this.passwordEncoder = passwordEncoder;

        // Configure RedisTemplate serializers for key-value storage
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    }

    /**
     * Generates a random 6-digit OTP code.
     *
     * @return 6-digit OTP as string
     */
    private String generateOtp() {
        return String.valueOf(100000 + new SecureRandom().nextInt(900000));
    }

    /**
     * Registers a new user with OTP verification.
     * Stores pending user and OTP in Redis temporarily (5 minutes).
     * Sends OTP to user's email for verification.
     *
     * @param req UserDTO containing registration information
     * @throws RuntimeException if email already exists
     */
    @Override
    public void register(UserDTO req) {
        // Check if email already exists in database
        if (userRepository.findUsersByEmail(req.getEmail()).isPresent())
            throw new RuntimeException("Email already exists");

        // Generate OTP code
        String otp = generateOtp();

        // Create pending user object with hashed password
        PendingUser pendingUser = new PendingUser(
                req.getName(),
                req.getEmail(),
                passwordEncoder.encode(req.getPassword())
        );

        // Store pending user in Redis with 5-minute expiration
        redisTemplate.opsForValue().set(
                "pending:user:" + req.getEmail(),
                pendingUser,
                Duration.ofMinutes(5)
        );

        // Store OTP in Redis with 5-minute expiration
        redisTemplate.opsForValue().set(
                "otp:" + req.getEmail(),
                otp,
                Duration.ofMinutes(5)
        );

        // Send OTP to user's email
        emailService.sendOtp(req.getEmail(), otp);

        System.out.println("OTP sent to email: " + req.getEmail()); // Optional logging
    }

    /**
     * Verifies OTP and completes user registration.
     * Validates OTP, creates user in database, and cleans up Redis data.
     *
     * @param req OtpVerifyRequest containing email and OTP
     * @throws RuntimeException if OTP is expired, invalid, or registration expired
     */
    @Override
    @Transactional
    public void verifyOtp(OtpVerifyRequest req) {
        String email = req.getEmail();

        // Retrieve OTP from Redis
        String cachedOtp = (String) redisTemplate.opsForValue().get("otp:" + email);
        if (cachedOtp == null)
            throw new RuntimeException("OTP expired");

        // Validate OTP
        if (!cachedOtp.equals(req.getOtp()))
            throw new RuntimeException("Invalid OTP");

        // Retrieve pending user from Redis
        PendingUser pendingUser = (PendingUser) redisTemplate.opsForValue().get("pending:user:" + email);
        if (pendingUser == null)
            throw new RuntimeException("Registration expired");

        // Create and save user in database
        User user = new User(
                pendingUser.getName(),
                pendingUser.getEmail(),
                pendingUser.getPassword()
        );

        userRepository.save(user);

        // Clean up temporary Redis data
        redisTemplate.delete("otp:" + email);
        redisTemplate.delete("pending:user:" + email);
    }
}