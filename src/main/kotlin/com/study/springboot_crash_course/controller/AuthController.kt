package com.study.springboot_crash_course.controller

import com.study.springboot_crash_course.security.AuthService
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    data class AuthRequest(
        @field:Email(message = "Invalid email format")
        val email: String,
        @field:Pattern(regexp = "^[A-Za-z0-9]{8,}\$", message = "Password must be at least 8 characters long and contain only letters and numbers")
        val password: String
    )
    data class RefreshRequest(
        val refreshToken: String
    )

    @PostMapping("/register")
    fun register(
        @RequestBody body: AuthRequest
    ){
        authService.register(body.email, body.password)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody body: AuthRequest
    ) : AuthService.TokenPair{
        return authService.login(body.email, body.password)
    }
    @PostMapping("/refresh")
    fun refresh(
        @RequestBody body: RefreshRequest
    ) : AuthService.TokenPair{
        return authService.refresh(body.refreshToken)
    }
}