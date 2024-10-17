package com.eLearningWebApp.eLearningWebApp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up CORS (Cross-Origin Resource Sharing) policies in the application.
 * This class allows for defining which origins, methods, and headers are allowed in cross-origin requests.
 */
@Configuration  // Marks this class as a configuration class for Spring.
public class CORSConfiguration {

    /**
     * Bean to configure CORS mappings for the application.
     * This method returns a WebMvcConfigurer that defines the CORS policy.
     * @return A WebMvcConfigurer instance with custom CORS configuration.
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {

            /**
             * Configures the CORS mappings for the application.
             * It allows requests from any origin and restricts the allowed HTTP methods to GET, POST, PUT, and DELETE.
             * @param registry The CorsRegistry to add the mapping rules.
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Applies CORS rules to all paths (/**).
                        .allowedMethods("GET", "POST", "PUT", "DELETE")  // Specifies allowed HTTP methods.
                        .allowedOrigins("*");  // Allows requests from any origin.
            }
        };
    }
}