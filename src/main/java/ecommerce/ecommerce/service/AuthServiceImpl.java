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

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, EmailService emailService,
                           RedisTemplate<String, Object> redisTemplate,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.redisTemplate = redisTemplate;
        this.passwordEncoder = passwordEncoder;

        // Configure RedisTemplate serializers
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    }

    private String generateOtp() {
        return String.valueOf(100000 + new SecureRandom().nextInt(900000));
    }

    @Override
    public void register(UserDTO req) {
        // التحقق من وجود الايميل
        if (userRepository.findUsersByEmail(req.getEmail()).isPresent())
            throw new RuntimeException("Email already exists");

        // توليد OTP
        String otp = generateOtp();

        // تخزين PendingUser في Redis
        PendingUser pendingUser = new PendingUser(
                req.getName(),
                req.getEmail(),
                passwordEncoder.encode(req.getPassword())
        );

        redisTemplate.opsForValue().set(
                "pending:user:" + req.getEmail(),
                pendingUser,
                Duration.ofMinutes(5)
        );

        // تخزين OTP في Redis
        redisTemplate.opsForValue().set(
                "otp:" + req.getEmail(),
                otp,
                Duration.ofMinutes(5)
        );

        // إرسال OTP بالإيميل
        emailService.sendOtp(req.getEmail(), otp);

        System.out.println("OTP sent to email: " + req.getEmail()); // optional logging
    }


    @Override
    @Transactional
    public void verifyOtp(OtpVerifyRequest req) {
        String email = req.getEmail();

        String cachedOtp = (String) redisTemplate.opsForValue().get("otp:" + email);
        if (cachedOtp == null)
            throw new RuntimeException("OTP expired");

        if (!cachedOtp.equals(req.getOtp()))
            throw new RuntimeException("Invalid OTP");

        PendingUser pendingUser = (PendingUser) redisTemplate.opsForValue().get("pending:user:" + email);
        if (pendingUser == null)
            throw new RuntimeException("Registration expired");

        // Save user in DB
        User user = new User(
                pendingUser.getName(),
                pendingUser.getEmail(),
                pendingUser.getPassword()
        );

        userRepository.save(user);

        // Delete temporary Redis data
        redisTemplate.delete("otp:" + email);
        redisTemplate.delete("pending:user:" + email);
    }
}
