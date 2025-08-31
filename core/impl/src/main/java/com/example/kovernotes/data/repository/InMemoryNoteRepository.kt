package com.example.kovernotes.data.repository

import com.example.kovernotes.domain.model.Note
import com.example.kovernotes.domain.repository.NoteRepository

class InMemoryNoteRepository : NoteRepository {
    private val notes = mutableListOf<Note>()
    private var nextId = 1L

    override fun addNote(title: String, content: String): Note {
        if (title.isBlank()) {
            throw IllegalArgumentException("Title must not be blank")
        }
        val note = Note(nextId++, title.trim(), content.trim())
        notes += note
        return note
    }

    override fun getNotes(): List<Note> = notes.toList()

    override fun clear() {
        notes.clear()
        nextId = 1L
    }
}
