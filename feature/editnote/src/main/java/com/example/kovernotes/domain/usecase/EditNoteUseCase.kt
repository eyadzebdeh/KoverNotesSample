package com.example.kovernotes.domain.usecase

import com.example.kovernotes.domain.model.Note
import com.example.kovernotes.domain.repository.NoteRepository
import javax.inject.Inject

class EditNoteUseCase @Inject constructor(private val repository: NoteRepository) {
    fun execute(id: Long, title: String, content: String): Note {
        val trimmedTitle = title.trim()
        val trimmedContent = content.trim()
        if (trimmedTitle.isBlank()) {
            throw IllegalArgumentException("Title must not be blank")
        }
        return repository.updateNote(id, trimmedTitle, trimmedContent)
    }
}
