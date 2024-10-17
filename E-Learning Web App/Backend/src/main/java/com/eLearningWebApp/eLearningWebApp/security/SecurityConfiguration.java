package com.eLearningWebApp.eLearningWebApp.security;


import com.eLearningWebApp.eLearningWebApp.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfiguration class configures security settings for the application,
 * including JWT authentication, password encoding, and method-level security.
 */
@Configuration  // Indicates this class is a Spring configuration class.
@EnableWebSecurity  // Enables Spring Security for web-based security.
@EnableMethodSecurity  // Enables method-level security annotations, such as @PreAuthorize.
public class SecurityConfiguration {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;  // Custom service to load user details.

    @Autowired
    private JWTAuthFilter jwtAuthFilter;  // Filter for JWT-based authentication.

    /**
     * Configures the security filter chain, including which endpoints are accessible without authentication,
     * session management policy (stateless for JWT), and the use of a custom JWT authentication filter.
     *
     * @param httpSecurity The HttpSecurity object to configure security settings.
     * @return A configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)  // Disables CSRF protection (usually not needed for stateless APIs).
                .cors(Customizer.withDefaults())  // Enables CORS with default settings.
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/**").permitAll()  // Permits all requests to authentication endpoints (e.g., login, registration).
                        .requestMatchers("/api/quiz-questions/**").authenticated()
                        .anyRequest().authenticated())  // Requires authentication for all other requests.
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Configures stateless session management (no sessions for JWT-based authentication).
                .authenticationProvider(authenticationProvider())  // Sets the custom authentication provider for user authentication.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);  // Adds the JWT authentication filter before the UsernamePasswordAuthenticationFilter.
        return httpSecurity.build();
    }


    /**
     * Configures the authentication provider, which is responsible for authenticating users based on the provided credentials.
     * Uses DaoAuthenticationProvider to authenticate using a UserDetailsService and a password encoder.
     *
     * @return The configured AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);  // Uses the custom user details service for authentication.
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());  // Sets the password encoder (BCrypt in this case).
        return daoAuthenticationProvider;
    }

    /**
     * Configures the password encoder used to hash passwords.
     * BCrypt is a strong hashing algorithm for securing passwords.
     *
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Returns a BCrypt password encoder for encoding and verifying passwords.
    }

    /**
     * Configures the authentication manager, which is responsible for processing authentication requests.
     *
     * @param authenticationConfiguration The authentication configuration object provided by Spring.
     * @return The AuthenticationManager instance.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();  // Retrieves the authentication manager from the configuration.
    }

}
