package ecommerce.ecommerce.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ----- Public endpoints -----
                        .requestMatchers("/api/products/all", "/api/products/{id}", "/api/users/register", "/api/users/login").permitAll()

                        // ----- Product management (ADMIN only) -----
                        .requestMatchers("/api/products/add", "/api/products/update/**", "/api/products/delete/**").hasAuthority("ADMIN")

                        // ----- Buying products (CUSTOMER or ADMIN) -----
                        .requestMatchers("/api/products/buy").hasAnyAuthority("CUSTOMER","ADMIN")

                        // ----- User endpoints -----
                        .requestMatchers(HttpMethod.GET, "/api/users").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyAuthority("CUSTOMER","ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasAnyAuthority("CUSTOMER","ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAnyAuthority("CUSTOMER","ADMIN")

                        // ----- Transaction endpoints -----
                        .requestMatchers("/api/transactions/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }



}
