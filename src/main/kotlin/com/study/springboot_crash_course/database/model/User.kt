package com.study.springboot_crash_course.database.model

import io.jsonwebtoken.security.Password
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class User(
    @Id val id: ObjectId = ObjectId(),
    @Indexed(unique = true)
    val email: String,
    val hashedPassword: String
)
