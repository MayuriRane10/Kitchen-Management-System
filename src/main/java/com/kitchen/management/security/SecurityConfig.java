
package com.kitchen.management.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth

                // PUBLIC AUTH APIs
                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/login",
                    "/api/auth/forgot-password",
                    "/api/auth/reset-password",
                    "/api/managers/login",
                    "/api/delivery-person/login"
                ).permitAll()

                // PUBLIC MANAGER DOCUMENTS
                .requestMatchers("/uploads/managers/**")
                .permitAll()

                // ADMIN APIs
                .requestMatchers("/api/admin/**")
                .hasRole("ADMIN")

                // MANAGER: GET ONLY THEIR ASSIGNED CLOUD KITCHEN
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/cloud-kitchen/getdatacloudkitchen"
                ).hasRole("MANAGER")

                // OTHER CLOUD KITCHEN APIs - ADMIN ONLY
                .requestMatchers("/api/cloud-kitchen/**")
                .hasRole("ADMIN")

                // MANAGER APIs
                .requestMatchers(
                    "/api/managers/getAllManagers",
                    "/api/managers/getManagerById/**",
                    "/api/managers/getAllCloudKitchens",
                    "/api/managers/getCloudKitchenById/**",
                    "/api/managers/getCloudKitchenStatus/**",
                    "/api/managers/openCloseCloudKitchen/**",
                    "/api/managers/getAllCuisines",
                    "/api/managers/getAllMeals",
                    "/api/managers/getAvailableMealsByCloudKitchen/**",
                    "/api/managers/cloud-kitchens/**",
                    "/api/managers/updateManager/**",
                    "/api/managers/changePassword/**"
                ).hasRole("MANAGER")

                // MANAGER DELETE - ADMIN ONLY
                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/managers/deleteManager/**"
                ).hasRole("ADMIN")

                // REVIEWS
                .requestMatchers("/api/reviews/**")
                .authenticated()

                // ADMIN AUTH
                .requestMatchers(
                    "/api/auth/change-password",
                    "/api/auth/logout"
                ).hasRole("ADMIN")

                // DELIVERY PERSON - ADMIN
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/delivery-person/saveDeliveryPerson"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/delivery-person/getAllDeliveryPersons",
                    "/api/delivery-person/getDeliveryPersonById/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/delivery-person/updateDeliveryPerson/**"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/delivery-person/deleteDeliveryPerson/**"
                ).hasRole("ADMIN")

                // MEALS - ADMIN
                .requestMatchers("/api/meals/**")
                .hasRole("ADMIN")

                // DELIVERY PERSON CHANGE PASSWORD
                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/delivery-person/changePassword/**"
                ).permitAll()

                // EVERYTHING ELSE
                .anyRequest().authenticated()
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
