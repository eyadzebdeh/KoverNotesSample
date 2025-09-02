package com.example.kovernotes.feature.editnote

import androidx.lifecycle.ViewModel
import com.example.kovernotes.domain.model.Note
import com.example.kovernotes.domain.repository.NoteRepository
import com.example.kovernotes.domain.usecase.EditNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val editNote: EditNoteUseCase,
    private val repository: NoteRepository
) : ViewModel() {

    data class State(
        val lastError: String? = null
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    fun edit(id: Long, title: String, content: String): Boolean {
        return try {
            editNote.execute(id, title, content)
            _state.value = _state.value.copy(lastError = null)
            true
        } catch (e: IllegalArgumentException) {
            _state.value = _state.value.copy(lastError = e.message)
            false
        }
    }

    fun getNote(id: Long): Note? = repository.getNotes().firstOrNull { it.id == id }
}
