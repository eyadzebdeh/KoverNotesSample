package com.example.kovernotes.feature.addnote

import androidx.lifecycle.ViewModel
import com.example.kovernotes.domain.usecase.AddNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class AddNoteState(
    val lastError: String? = null
)

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val addNote: AddNoteUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AddNoteState())
    val state: StateFlow<AddNoteState> = _state.asStateFlow()

    fun add(title: String, content: String): Boolean {
        return try {
            addNote.execute(title, content)
            _state.value = _state.value.copy(lastError = null)
            true
        } catch (e: IllegalArgumentException) {
            _state.value = _state.value.copy(lastError = e.message)
            false
        }
    }
}
