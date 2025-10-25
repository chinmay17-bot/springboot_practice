// kotlin
package com.study.springboot_crash_course.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.bson.types.ObjectId
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(private val jwtService: JwtService) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substringAfter("Bearer ").trim()
            try {
                if (jwtService.validateAccessToken(token)) {
                    val userId = jwtService.getUserIdFromToken(token)?.toString() ?: ""
                    // only set authentication when userId is a valid ObjectId hex
                    if (ObjectId.isValid(userId)) {
                        val auth = UsernamePasswordAuthenticationToken(userId, null, emptyList())
                        SecurityContextHolder.getContext().authentication = auth
                    }
                }
            } catch (ex: Exception) {
                // don't set authentication if token parsing fails; let downstream handle unauthenticated access
            }
        }
        filterChain.doFilter(request, response)
    }
}
