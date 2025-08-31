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
}
