package com.example.kovernotes.domain.repository

import com.example.kovernotes.domain.model.Note

interface NoteRepository {
    fun addNote(title: String, content: String): Note
    fun getNotes(): List<Note>
    fun clear()
}
