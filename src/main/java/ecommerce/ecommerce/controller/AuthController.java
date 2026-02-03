package ecommerce.ecommerce.controller;

import ecommerce.ecommerce.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller handling authentication endpoints for the e-commerce application.
 * Provides login functionality and JWT token generation.
 */
@RestController
@RequestMapping("/auth") // Base URL path for all authentication endpoints
public class AuthController {

    // Spring Security's authentication manager - validates user credentials
    private final AuthenticationManager authManager;

    // Utility class for generating and validating JWT tokens
    private final JwtUtil jwtUtil;

    /**
     * Constructor injection of required dependencies.
     * Spring automatically provides these beans at runtime.
     *
     * @param authManager Handles the authentication process
     * @param jwtUtil Handles JWT token operations
     */
    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Login endpoint that authenticates users and returns a JWT token.
     * POST /auth/login
     *
     * @param req Login request containing email and password
     * @return ResponseEntity with JWT token if authentication succeeds
     * @throws AuthenticationException if credentials are invalid
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest req) {

        // Attempt to authenticate the user with provided credentials
        // This triggers Spring Security's authentication process (UserDetailsService, password verification, etc.)
        // Throws exception if authentication fails
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.email(), req.password())
        );

        // Extract the authenticated user's details from the Authentication object
        UserDetails user = (UserDetails) auth.getPrincipal();

        // Generate a JWT token for the authenticated user
        // This token will be used for subsequent authenticated requests
        String token = jwtUtil.generateToken(user);

        // Return HTTP 200 OK with the JWT token in the response body
        return ResponseEntity.ok(new JwtResponse(token));
    }
}

/**
 * Record representing the login request payload.
 * Contains user credentials submitted during login.
 */
record LoginRequest(String email, String password) {}

/**
 * Record representing the login response payload.
 * Contains the JWT token to be returned to the client.
 */
record JwtResponse(String token) {}