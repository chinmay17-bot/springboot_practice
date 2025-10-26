package com.study.springboot_crash_course

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


//On all the annotation that has been created for validation, this class will handle the validation globally. to throw the respective error messages.

@RestControllerAdvice
class GobalValidationHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(e: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = e.bindingResult.allErrors.map{
            it.defaultMessage ?: "Invalid value"
        }
        return ResponseEntity.status(400).body(mapOf("errors" to errors))
    }
}