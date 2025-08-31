package com.example.kovernotes.feature.addnote

import com.example.kovernotes.data.repository.InMemoryNoteRepository
import com.example.kovernotes.domain.usecase.AddNoteUseCase
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AddNoteViewModelTest {
    @Test
    fun add_success_returnsTrue_andClearsError() {
        val repo = InMemoryNoteRepository()
        val vm = AddNoteViewModel(AddNoteUseCase(repo))

        val result = vm.add("Title", "Body")

        assertThat(result).isTrue()
        assertThat(vm.state.value.lastError).isNull()
    }

    @Test
    fun add_blankTitle_setsLastError_andReturnsFalse() {
        val repo = InMemoryNoteRepository()
        val vm = AddNoteViewModel(AddNoteUseCase(repo))

        val result = vm.add("   ", "Body")

        assertThat(result).isFalse()
        assertThat(vm.state.value.lastError).isEqualTo("Title must not be blank")
    }
}
