package com.example.kovernotes.data.repository

import com.example.kovernotes.domain.model.Note
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class InMemoryNoteRepositoryEditTest {
    private lateinit var repo: InMemoryNoteRepository

    @Before
    fun setup() {
        repo = InMemoryNoteRepository()
    }

    @Test
    fun update_existing_changesStoredValues_withoutTrimming() {
        val note = repo.addNote("  Title  ", "  Body  ")

        val updated = repo.updateNote(note.id, "  New Title  ", "  New Body  ")

        assertThat(updated.id).isEqualTo(note.id)
        assertThat(updated.title).isEqualTo("  New Title  ")
        assertThat(updated.content).isEqualTo("  New Body  ")
        val stored = repo.getNotes().single()
        assertThat(stored).isEqualTo(updated)
    }

    @Test(expected = IllegalArgumentException::class)
    fun update_nonExisting_throws() {
        repo.updateNote(42L, "X", "Y")
    }
}
