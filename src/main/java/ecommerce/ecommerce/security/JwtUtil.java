package ecommerce.ecommerce.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utility class for JWT token operations.
 * Handles token generation, validation, and parsing.
 */
@Component
public class JwtUtil {

    // Secret key for signing JWT tokens (must be at least 32 bytes for HS256)
    @Value("${jwt.secret}")
    private String SECRET;

    // Token expiration time: 24 hours in milliseconds
    @Value("${jwt.expiration}")
    private long EXPIRATION;

    /**
     * Generates a JWT token for authenticated user.
     *
     * @param user The authenticated user details
     * @return JWT token string
     */
    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername()) // Set email as subject
                .setIssuedAt(new Date()) // Set current time as issue date
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // Set expiration
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()),
                        SignatureAlgorithm.HS256) // Sign with secret key
                .compact(); // Build and return token string
    }

    /**
     * Extracts username (email) from JWT token.
     *
     * @param token The JWT token
     * @return The username (email) from token
     */
    public String extractUsername(String token) {
        return parse(token).getSubject();
    }

    /**
     * Validates JWT token against user details and expiration.
     *
     * @param token The JWT token to validate
     * @param user The user details to validate against
     * @return true if token is valid and not expired, false otherwise
     */
    public boolean isValid(String token, UserDetails user) {
        return extractUsername(token).equals(user.getUsername())
                && !parse(token).getExpiration().before(new Date());
    }

    /**
     * Parses JWT token and extracts claims.
     *
     * @param token The JWT token to parse
     * @return Claims object containing token data
     */
    private Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes()) // Set secret key for verification
                .build()
                .parseClaimsJws(token) // Parse and verify token
                .getBody(); // Extract claims
    }
}