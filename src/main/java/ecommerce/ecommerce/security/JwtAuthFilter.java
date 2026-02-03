package ecommerce.ecommerce.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * JWT Authentication Filter that intercepts every HTTP request.
 * Validates JWT tokens and sets authentication in Spring Security context.
 * Extends OncePerRequestFilter to ensure it runs once per request.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // Utility for JWT operations (validation, extraction)
    private final JwtUtil jwtUtil;

    // Service to load user details from database
    private final CustomUserDetailsService uds;

    // Constructor
    public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService uds) {
        this.jwtUtil = jwtUtil;
        this.uds = uds;
    }

    /**
     * Filters incoming requests to validate JWT tokens.
     * Executes for every request except public endpoints.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @param chain The filter chain to continue processing
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // Skip JWT validation for public endpoints (login, registration, OTP verification)
        if (path.startsWith("/auth") || path.equals("/api/users/register") || path.equals("/api/users/verify-otp")) {
            chain.doFilter(request, response);
            return; // Important: stop processing for public endpoints
        }

        // Extract JWT token from Authorization header
        String header = request.getHeader("Authorization");

        // Check if Authorization header exists and starts with "Bearer "
        if (header != null && header.startsWith("Bearer ")) {
            // Extract token (remove "Bearer " prefix)
            String token = header.substring(7);

            // Extract username (email) from token
            String email = jwtUtil.extractUsername(token);

            // If email exists and user is not already authenticated
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Load user details from database
                var userDetails = uds.loadUserByUsername(email);

                // Validate token against user details
                if (jwtUtil.isValid(token, userDetails)) {
                    // Create authentication token with user details and authorities
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // Set additional authentication details (IP address, session ID, etc.)
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set authentication in Spring Security context
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        // Continue filter chain
        chain.doFilter(request, response);
    }
}