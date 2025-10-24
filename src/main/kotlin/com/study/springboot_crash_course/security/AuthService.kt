package com.study.springboot_crash_course.security

import com.study.springboot_crash_course.database.model.User
import com.study.springboot_crash_course.database.repository.UserRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder,
) {

    data class TokenPair(
        val accessToken: String,
        val refreshToken: String
    )
    fun register(email:String , password : String): User {
        return userRepository.save(
            User(
                email=email,
                hashedPassword = hashEncoder.encode(password),
            )
        )
    }

    fun login(email: String, password: String): TokenPair {
        val user= userRepository.findByEmail(email)
            ?: throw BadCredentialsException("User not found")

        if(!hashEncoder.matches(password, user.hashedPassword)){
            throw BadCredentialsException("Invalid password")
        }

        val newAccessToken = jwtService.generateAccessToken(user.id.toHexString())
        val newRefreshToken = jwtService.generateRefreshToken(user.id.toHexString())

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

}