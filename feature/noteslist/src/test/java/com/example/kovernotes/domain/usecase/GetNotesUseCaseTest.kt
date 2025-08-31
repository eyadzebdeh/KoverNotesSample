package com.example.kovernotes.domain.usecase

import com.example.kovernotes.data.repository.FakeNoteRepository
import com.example.kovernotes.domain.model.Note
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class GetNotesUseCaseTest {
    @Test
    fun execute_returnsAllNotes() {
        val repo = FakeNoteRepository(initial = listOf(
            Note(1, "A", "a"),
            Note(2, "B", "b")
        ))
        val useCase = GetNotesUseCase(repo)

        val notes = useCase.execute()

        assertThat(notes.map { it.id }).containsExactly(1L, 2L).inOrder()
    }
}
