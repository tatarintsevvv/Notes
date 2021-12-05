package com.notes.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.NoteDatabase
import com.notes.data.NoteDbo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class NoteListViewModel @Inject constructor(
    private val noteDatabase: NoteDatabase
) : ViewModel() {

// смысл делать List nullable?
    val notes = MutableLiveData<List<NoteListItem>>()
// непонятное задвоение кода, какой смысл от вьюмодели прятать ее же данные
// val notes: LiveData<List<NoteListItem>?> = _notes

    private val _navigateToNoteCreation = MutableLiveData<NoteDbo?>()
    val navigateToNoteCreation: LiveData<NoteDbo?> = _navigateToNoteCreation

    init {
        viewModelScope.launch(Dispatchers.IO) {
            updateNotes()
        }
    }

    private fun updateNotes() {
        notes.postValue(
            noteDatabase.noteDao().getAll().map {
                NoteListItem(
                    id = it.id,
                    title = it.title,
                    content = it.content,
                    createdAt = it.createdAt,
                    modifiedAt = it.modifiedAt
                )
            }
        )
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDatabase.noteDao().deleteNote(noteId)
            updateNotes()
        }

    }

    fun editNote(note: NoteDbo) {
        _navigateToNoteCreation.postValue(note)

    }
    fun onCreateNoteClick() {
        _navigateToNoteCreation.postValue(null)
    }

}

data class NoteListItem(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime
)