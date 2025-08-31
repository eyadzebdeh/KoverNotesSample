package com.example.kovernotes.domain.usecase

import com.example.kovernotes.data.repository.FakeNoteRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AddNoteUseCaseTest {
    @Test
    fun execute_addsNote_andReturnsCreatedNote() {
        val repo = FakeNoteRepository()
        val useCase = AddNoteUseCase(repo)

        val note = useCase.execute("Hello", "World")

        assertThat(note.id).isEqualTo(1L)
        assertThat(repo.getNotes()).containsExactly(note)
    }

    @Test(expected = IllegalArgumentException::class)
    fun execute_blankTitle_throws() {
        val repo = FakeNoteRepository()
        val useCase = AddNoteUseCase(repo)

        useCase.execute("   ", "x")
    }

    @Test
    fun execute_trims_title_and_content() {
        val repo = FakeNoteRepository()
        val useCase = AddNoteUseCase(repo)

        val note = useCase.execute("  Title  ", "  Content  ")

        assertThat(note.title).isEqualTo("Title")
        assertThat(note.content).isEqualTo("Content")
    }
}
