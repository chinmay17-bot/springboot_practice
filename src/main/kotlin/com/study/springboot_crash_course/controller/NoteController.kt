package com.study.springboot_crash_course.controller

import com.study.springboot_crash_course.controller.NoteController.NoteResponse
import com.study.springboot_crash_course.database.model.Note
import com.study.springboot_crash_course.database.repository.NoteRepository
import jakarta.validation.constraints.Positive
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/notes")
class NoteController(
    private val repository: NoteRepository
) {
    data class NoteRequest(
        val id: String?,
        val title: String,
        val content: String,
        val color: Long,
        //This is not safe as risks security
        val ownerId: String
    )

    data class NoteResponse(
        val id: String,
        val title: String,
        val content: String,
        val color: Long,
        val createdAt: Instant,
    )


    //wont work normally due to csrf safety (must read about it)
    //for this to be working we need to create a seqConfig.kt file
    @PostMapping
    fun save(body: NoteRequest): NoteResponse {
        val note = repository.save(
            Note(
                //syntax is important this shows
                //if note exist then change the string to ObjectId else create a new ObjectId
                id = body.id?.let { ObjectId(it) } ?: ObjectId.get(),
                title = body.title,
                content = body.content,
                color = body.color,
                createdAt = Instant.now(),
                userId = ObjectId(body.ownerId)
            )
        )
        return note.toResponse()
    }

    @GetMapping
    fun findByOwnerId(
        @RequestParam(required = true) ownerId: String
    ): List<NoteResponse> {
        return repository.findByUserId(ObjectId(ownerId)).map {
            it.toResponse()
        }
    }
}

private fun Note.toResponse(): NoteController.NoteResponse {
    return NoteResponse(
        id = id.toHexString(),
        title = title,
        content = content,
        color = color,
        createdAt = createdAt
    )
}

