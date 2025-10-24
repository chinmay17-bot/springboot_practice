package com.study.springboot_crash_course.database.repository

import com.study.springboot_crash_course.database.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, ObjectId> {
    //Find a user via this email can be nullable
    fun findByEmail(email: String): User?
}