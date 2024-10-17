package com.eLearningWebApp.eLearningWebApp.security;

import com.eLearningWebApp.eLearningWebApp.service.CustomUserDetailsService;
import com.eLearningWebApp.eLearningWebApp.utilities.JWTUtilities;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWTAuthFilter is a custom filter that intercepts each HTTP request once per request
 * to authenticate the user based on the JWT token provided in the Authorization header.
 */
@Component  // Marks this class as a Spring-managed component (bean).
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtilities jwtUtils;  // Utility class for JWT token operations like extraction and validation.

    @Autowired
    private CustomUserDetailsService customUserDetailsService;  // Service to load user details from the database.

    /**
     * Filters incoming requests to check if there is a valid JWT token in the Authorization header.
     * If a valid token is found, it authenticates the user and sets the authentication in the security context.
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param filterChain The filter chain to pass the request/response to the next filter.
     * @throws ServletException If an exception occurs during filtering.
     * @throws IOException If an input/output error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Extracts the Authorization header from the request
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        // If the Authorization header is missing or empty, pass the request to the next filter in the chain
        if (authHeader == null || authHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extracts the JWT token from the Authorization header (assumes the token starts after "Bearer ")
        jwtToken = authHeader.substring(7);
        userEmail = jwtUtils.extractUsername(jwtToken);  // Extracts the username (email) from the token

        // If a user email was extracted and there is no authentication in the security context, proceed with validation
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Loads user details from the database using the extracted email
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

            // If the token is valid for the user, set up authentication
            if (jwtUtils.isValidToken(jwtToken, userDetails)) {
                // Creates a new security context and an authentication token for the user
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());  // Create an authentication token

                // Sets additional details for the authentication token (like remote address, session ID)
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);  // Sets the authenticated user in the security context
                SecurityContextHolder.setContext(securityContext);  // Stores the security context
            }
        }
        // Passes the request/response to the next filter in the chain
        filterChain.doFilter(request, response);
    }
}