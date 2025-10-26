package com.study.springboot_crash_course.security

import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


//Must have for post requests for production based auth
@Configuration
class SecurityConfig (private val jwtAuthFilter: JwtAuthFilter) {
    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .csrf { csrf -> csrf.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/auth/**").permitAll() // this path is allowed to all without authentication

                    //with this error 403 will be immediately returned for incorrect paths
                    .dispatcherTypeMatchers(
                        DispatcherType.ERROR,
                        DispatcherType.FORWARD
                    )
                    .permitAll()
                    //any other request must be authenticated
                    .anyRequest().authenticated()
            }

            //if authentication fails return 401 unauthorized instead of 403 forbidden
            .exceptionHandling { configurer ->
                configurer
                    .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            }
            //make sure these filter are applied
            .addFilterBefore(jwtAuthFilter , UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}