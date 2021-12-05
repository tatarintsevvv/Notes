package com.notes.ui.details

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

class NoteDetailsViewModel @Inject constructor(
    private val noteDatabase: NoteDatabase
) : ViewModel() {

    private val _navigateToNoteList = MutableLiveData<Unit?>()
    val navigateToNoteList: LiveData<Unit?> = _navigateToNoteList

    fun addNote(title: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val now = LocalDateTime.now()
            noteDatabase.noteDao().insertAll(
                NoteDbo(
                    title = title.replaceFirstChar { char -> char.uppercase() },
                    content = description.replaceFirstChar { char -> char.uppercase() },
                    createdAt = now,
                    modifiedAt = now
                )
            )
            _navigateToNoteList.postValue(Unit)
        }
    }

    fun changeNote(id: Long,title: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val now = LocalDateTime.now()
            noteDatabase.noteDao().changeNote(
                    title = title.replaceFirstChar { char -> char.uppercase() },
                    content = description.replaceFirstChar { char -> char.uppercase() },
                    modifiedAt = now,
                    noteId = id
            )
            _navigateToNoteList.postValue(Unit)
        }

    }
}
