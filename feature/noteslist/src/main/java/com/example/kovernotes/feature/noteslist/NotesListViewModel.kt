package com.example.kovernotes.feature.noteslist

import androidx.lifecycle.ViewModel
import com.example.kovernotes.domain.model.Note
import com.example.kovernotes.domain.usecase.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class NotesListState(
    val notes: List<Note> = emptyList()
)

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val getNotes: GetNotesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(NotesListState())
    val state: StateFlow<NotesListState> = _state.asStateFlow()

    fun load() {
        _state.value = _state.value.copy(notes = getNotes.execute())
    }
}
