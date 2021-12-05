package com.notes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDateTime

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAll(): List<NoteDbo>

    @Insert
    fun insertAll(vararg notes: NoteDbo)

    @Query("DELETE FROM notes WHERE id = :noteId")
    fun deleteNote(noteId: Long)

    @Query("UPDATE notes SET title = :title, content = :content, modifiedAt = :modifiedAt WHERE id = :noteId")
    fun changeNote(title: String, content: String, modifiedAt: LocalDateTime, noteId: Long)
}