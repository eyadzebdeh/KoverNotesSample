package com.example.kovernotes.data.repository

import com.example.kovernotes.domain.model.Note
import com.example.kovernotes.domain.repository.NoteRepository

class FakeNoteRepository(
    private val initial: List<Note> = emptyList()
) : NoteRepository {
    private val notes = initial.toMutableList()
    private var nextId: Long = (notes.maxOfOrNull { it.id } ?: 0L) + 1

    override fun addNote(title: String, content: String): Note {
        val note = Note(nextId++, title, content)
        notes += note
        return note
    }

    override fun getNotes(): List<Note> = notes.toList()

    override fun updateNote(id: Long, title: String, content: String): Note {
        val index = notes.indexOfFirst { it.id == id }
        if (index == -1) throw IllegalArgumentException("Note with id=$id not found")
        val updated = Note(id, title, content)
        notes[index] = updated
        return updated
    }

    override fun clear() {
        notes.clear()
        nextId = 1
    }
}
