package com.study.springboot_crash_course.security

import com.study.springboot_crash_course.database.model.RefreshToken
import com.study.springboot_crash_course.database.model.User
import com.study.springboot_crash_course.database.repository.RefreshTokenRepository
import com.study.springboot_crash_course.database.repository.UserRepository
import io.jsonwebtoken.lang.Objects
import org.bson.types.ObjectId
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder,
    private val refreshTokenRepository: RefreshTokenRepository,
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

        storeRefreshToken(user.id, newRefreshToken)

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    private fun storeRefreshToken(userId: ObjectId, rawToken: String ) {
        val hashed= hashToken(rawToken)
        val expiryMs = jwtService.refreshTokenValidityMs
        val expiresAt = Instant.now().plusMillis(expiryMs)

        refreshTokenRepository.save(
            RefreshToken(
                userId = userId,
                expiresAt = expiresAt,
                hashedToken = hashed
            )
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.toByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }

}