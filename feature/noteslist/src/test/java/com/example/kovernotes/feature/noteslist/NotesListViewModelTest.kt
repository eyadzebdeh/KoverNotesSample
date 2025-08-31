package com.example.kovernotes.feature.noteslist

import com.example.kovernotes.data.repository.FakeNoteRepository
import com.example.kovernotes.domain.model.Note
import com.example.kovernotes.domain.usecase.GetNotesUseCase
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class NotesListViewModelTest {
    @Test
    fun load_populatesStateWithNotes() {
        val repo = FakeNoteRepository(initial = listOf(
            Note(1, "One", "1"),
            Note(2, "Two", "2")
        ))
        val vm = NotesListViewModel(GetNotesUseCase(repo))

        vm.load()

        assertThat(vm.state.value.notes.map { it.id }).containsExactly(1L, 2L).inOrder()
    }
}
