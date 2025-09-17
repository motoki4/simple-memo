package com.k.simplememo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.k.simplememo.data.NoteRepository
import com.k.simplememo.data.local.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.notes.collect { notes ->
                _uiState.update { it.copy(notes = notes) }
            }
        }
    }

    fun onContentChange(value: String) {
        _uiState.update { it.copy(noteContent = value) }
    }

    fun saveNote() {
        val content = _uiState.value.noteContent.trim()
        if (content.isEmpty()) return
        viewModelScope.launch {
            repository.addNote(content)
            _uiState.update { it.copy(noteContent = "") }
        }
    }

    fun onEditNote(note: Note) {
        _uiState.update {
            it.copy(
                editingNote = note,
                editingContent = note.content,
                showEditDialog = true
            )
        }
    }

    fun onEditingContentChange(value: String) {
        _uiState.update { it.copy(editingContent = value) }
    }

    fun updateNote() {
        val editingNote = _uiState.value.editingNote ?: return
        val updatedContent = _uiState.value.editingContent.trim()
        if (updatedContent.isEmpty()) {
            dismissDialog()
            return
        }
        viewModelScope.launch {
            repository.updateNote(editingNote.copy(content = updatedContent))
            dismissDialog()
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
            if (_uiState.value.editingNote?.id == note.id) {
                dismissDialog()
            }
        }
    }

    fun dismissDialog() {
        _uiState.update {
            it.copy(
                showEditDialog = false,
                editingNote = null,
                editingContent = ""
            )
        }
    }

    data class NoteUiState(
        val noteContent: String = "",
        val notes: List<Note> = emptyList(),
        val showEditDialog: Boolean = false,
        val editingNote: Note? = null,
        val editingContent: String = ""
    )

    companion object {
        fun provideFactory(repository: NoteRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                        return NoteViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
