package com.study.springboot_crash_course.database.model

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("refresh-token")
data class RefreshToken(
    val userId: ObjectId,
    //This indexed must bwe from mongodb
    // not really ness. but improves performance as it is kept separately in mongodb
    @Indexed(expireAfter = "0s")
    val expiresAt: Instant,
    val hashedToken: String,
    val createdAt: Instant = Instant.now()
)
