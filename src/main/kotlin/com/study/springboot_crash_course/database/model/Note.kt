package com.study.springboot_crash_course.database.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

//this tells mongo as its a real document
@Document("Notes")
data class Note(
    val title: String,
    val content: String,
    val color: Long,
    val createdAt: Instant,
    val userId: ObjectId,
    //This tells mongodb that this is the main id if not then notes doc
    // wont be accessible by mongo as we dont have any access to them
    @Id val id: ObjectId = ObjectId.get()
)
