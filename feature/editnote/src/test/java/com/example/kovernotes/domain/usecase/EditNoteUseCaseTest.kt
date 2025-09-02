package com.example.kovernotes.domain.usecase

import com.example.kovernotes.data.repository.FakeNoteRepository
import com.example.kovernotes.domain.model.Note
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EditNoteUseCaseTest {
    @Test
    fun execute_updatesNote_andReturnsUpdatedNote() {
        val repo = FakeNoteRepository(initial = listOf(Note(1, "Old", "old")))
        val useCase = EditNoteUseCase(repo)

        val updated = useCase.execute(1, "New", "new")

        assertThat(updated.id).isEqualTo(1L)
        assertThat(updated.title).isEqualTo("New")
        assertThat(repo.getNotes().single()).isEqualTo(updated)
    }

    @Test(expected = IllegalArgumentException::class)
    fun execute_blankTitle_throws() {
        val repo = FakeNoteRepository(initial = listOf(Note(1, "Old", "old")))
        val useCase = EditNoteUseCase(repo)
        useCase.execute(1, "   ", "x")
    }

    @Test
    fun execute_trims_title_and_content() {
        val repo = FakeNoteRepository(initial = listOf(Note(1, "Old", "old")))
        val useCase = EditNoteUseCase(repo)

        val updated = useCase.execute(1, "  Title  ", "  Content  ")

        assertThat(updated.title).isEqualTo("Title")
        assertThat(updated.content).isEqualTo("Content")
        assertThat(repo.getNotes().single()).isEqualTo(updated)
    }
}
