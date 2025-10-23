package com.study.springboot_crash_course.database.repository

import com.study.springboot_crash_course.controller.NoteController
import com.study.springboot_crash_course.database.model.Note
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface NoteRepository : MongoRepository<Note, ObjectId> {
    fun findByUserId(ownerId: ObjectId): List<Note>
}