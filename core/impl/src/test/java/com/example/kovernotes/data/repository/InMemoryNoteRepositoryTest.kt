package com.example.kovernotes.data.repository

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class InMemoryNoteRepositoryTest {
    private lateinit var repo: InMemoryNoteRepository

    @Before
    fun setup() {
        repo = InMemoryNoteRepository()
    }

    @Test
    fun addNote_storesNote_andAssignsIncrementalId_withoutAlteringFields() {
        val note1 = repo.addNote("  Title  ", "  Content  ")
        val note2 = repo.addNote("Second", "C2")

        assertThat(note1.id).isEqualTo(1L)
        assertThat(note2.id).isEqualTo(2L)
        // Repository should not trim; fields are stored as provided
        assertThat(note1.title).isEqualTo("  Title  ")
        assertThat(note1.content).isEqualTo("  Content  ")
        assertThat(repo.getNotes()).containsExactly(note1, note2).inOrder()
    }

    @Test
    fun clear_resetsStorage_andIdCounter() {
        repo.addNote("A", "B")
        repo.clear()
        assertThat(repo.getNotes()).isEmpty()
        val note = repo.addNote("Z", "Y")
        assertThat(note.id).isEqualTo(1L)
    }
}
