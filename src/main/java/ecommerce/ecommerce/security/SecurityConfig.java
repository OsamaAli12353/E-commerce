package ecommerce.ecommerce.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Spring Security configuration class.
 * Configures authentication, authorization, JWT filtering, and security policies.
 */
@Configuration
public class SecurityConfig {

    // JWT authentication filter
    private final JwtAuthFilter jwtFilter;

    // Constructor
    public SecurityConfig(JwtAuthFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Creates password encoder bean for hashing passwords.
     * Uses BCrypt with strength 12.
     *
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Creates authentication manager bean.
     * Used for authenticating users during login.
     *
     * @param config Authentication configuration
     * @return AuthenticationManager instance
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configures the security filter chain.
     * Defines authorization rules, session policy, and filter order.
     *
     * @param http HttpSecurity to configure
     * @return SecurityFilterChain instance
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF protection (not needed for stateless JWT authentication)
                .csrf(csrf -> csrf.disable())

                // Set session management to STATELESS (no server-side sessions)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configure authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - no authentication required
                        .requestMatchers(
                                "/auth/**",                 // Login endpoint
                                "/api/users/register",      // Registration endpoint
                                "/actuator/**",             // Spring Boot Actuator
                                "/swagger-ui/**",           // Swagger UI
                                "/v3/api-docs/**",          // API documentation
                                "/api/users/verify-otp"     // OTP verification
                        ).permitAll()

                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )

                // Add JWT filter before Spring Security's authentication filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}