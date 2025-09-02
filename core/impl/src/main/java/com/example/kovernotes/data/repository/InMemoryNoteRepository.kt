package com.example.kovernotes.data.repository

import com.example.kovernotes.domain.model.Note
import com.example.kovernotes.domain.repository.NoteRepository

class InMemoryNoteRepository : NoteRepository {
    private val notes = mutableListOf<Note>()
    private var nextId = 1L

    override fun addNote(title: String, content: String): Note {
        // Repository should not apply business rules like trimming; it stores as provided.
        val note = Note(nextId++, title, content)
        notes += note
        return note
    }

    override fun getNotes(): List<Note> = notes.toList()

    override fun updateNote(id: Long, title: String, content: String): Note {
        val index = notes.indexOfFirst { it.id == id }
        if (index == -1) throw IllegalArgumentException("Note with id=$id not found")
        val updated = Note(id = id, title = title, content = content)
        notes[index] = updated
        return updated
    }

    override fun clear() {
        notes.clear()
        nextId = 1L
    }
}
