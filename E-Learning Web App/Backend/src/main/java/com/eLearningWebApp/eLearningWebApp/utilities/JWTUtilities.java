package com.eLearningWebApp.eLearningWebApp.utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for generating, validating, and extracting information from JWT tokens.
 * This class handles JWT creation and verification using HMAC-SHA256 encryption.
 */
@Service  // Marks this class as a Spring service, making it available for dependency injection.
public class JWTUtilities {

    private static final long EXPIRATION_TIME = 1000 * 600 * 7 * 24; // Token expiration time (7 days in milliseconds).
    private final SecretKey key;  // Secret key for signing and verifying JWT tokens.

    /**
     * Constructor initializes the secret key for signing JWT tokens.
     * The secret key is decoded from a Base64 string and used with HmacSHA256.
     */
    public JWTUtilities() {
        String secretString = "974683758347635798234785673492Y8365T349857G23876T4738476987238476928364837659346875934786593784T593475T823";  // Example secret key.
        byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));  // Decodes the Base64 secret string.
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");  // Creates the secret key using HmacSHA256 algorithm.
    }

    /**
     * Generates a JWT token for the provided UserDetails.
     * The token includes the username, issue date, expiration date, and is signed with the secret key.
     * @param userDetails The UserDetails object containing user information.
     * @return The generated JWT token as a String.
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())  // Sets the username as the subject of the token.
                .issuedAt(new Date(System.currentTimeMillis()))  // Sets the issue date to the current time.
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // Sets the expiration date.
                .signWith(key)  // Signs the token with the secret key.
                .compact();  // Builds the token and returns it as a String.
    }

    /**
     * Extracts the username (subject) from the provided JWT token.
     * @param token The JWT token from which to extract the username.
     * @return The username (subject) extracted from the token.
     */
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);  // Extracts the "subject" (username) claim from the token.
    }

    /**
     * Extracts a specific claim from the JWT token using a function.
     * @param token The JWT token from which to extract claims.
     * @param claimsTFunction A function that specifies which claim to extract.
     * @param <T> The type of the claim to extract.
     * @return The extracted claim.
     */
    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());  // Parses the token and extracts the desired claim.
    }

    /**
     * Validates the JWT token by checking the username and ensuring the token is not expired.
     * @param token The JWT token to validate.
     * @param userDetails The UserDetails to match against the token's username.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);  // Extracts the username from the token.
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));  // Checks if the username matches and if the token is not expired.
    }

    /**
     * Checks if the JWT token has expired.
     * @param token The JWT token to check.
     * @return True if the token has expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());  // Checks if the expiration date is before the current time.
    }

}
