package com.example.kovernotes.domain.usecase

import com.example.kovernotes.domain.model.Note
import com.example.kovernotes.domain.repository.NoteRepository
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(private val repository: NoteRepository) {
    fun execute(): List<Note> = repository.getNotes()
}
