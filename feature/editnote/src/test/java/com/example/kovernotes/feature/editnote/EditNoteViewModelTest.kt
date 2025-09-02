package com.example.kovernotes.feature.editnote

import com.example.kovernotes.data.repository.FakeNoteRepository
import com.example.kovernotes.domain.model.Note
import com.example.kovernotes.domain.usecase.EditNoteUseCase
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EditNoteViewModelTest {

    @Test
    fun edit_success_returnsTrue_andClearsError_andUpdatesRepo() {
        val repo = FakeNoteRepository(initial = listOf(Note(1, "Old", "old")))
        val vm = EditNoteViewModel(EditNoteUseCase(repo), repo)

        val result = vm.edit(1, "New", "new")

        assertThat(result).isTrue()
        assertThat(vm.state.value.lastError).isNull()
        val stored = repo.getNotes().single()
        assertThat(stored.title).isEqualTo("New")
        assertThat(stored.content).isEqualTo("new")
    }

    @Test
    fun edit_blankTitle_setsLastError_andReturnsFalse() {
        val repo = FakeNoteRepository(initial = listOf(Note(1, "Old", "old")))
        val vm = EditNoteViewModel(EditNoteUseCase(repo), repo)

        val result = vm.edit(1, "   ", "x")

        assertThat(result).isFalse()
        assertThat(vm.state.value.lastError).isEqualTo("Title must not be blank")
        // Ensure repository not modified
        val stored = repo.getNotes().single()
        assertThat(stored.title).isEqualTo("Old")
        assertThat(stored.content).isEqualTo("old")
    }

    @Test
    fun getNote_returnsNote_whenExists() {
        val repo = FakeNoteRepository(initial = listOf(Note(1, "A", "a"), Note(2, "B", "b")))
        val vm = EditNoteViewModel(EditNoteUseCase(repo), repo)

        val note = vm.getNote(2)

        assertThat(note).isNotNull()
        assertThat(note!!.id).isEqualTo(2L)
        assertThat(note.title).isEqualTo("B")
    }

    @Test
    fun getNote_returnsNull_whenNotFound() {
        val repo = FakeNoteRepository(initial = listOf(Note(1, "A", "a")))
        val vm = EditNoteViewModel(EditNoteUseCase(repo), repo)

        val note = vm.getNote(42)

        assertThat(note).isNull()
    }
}
