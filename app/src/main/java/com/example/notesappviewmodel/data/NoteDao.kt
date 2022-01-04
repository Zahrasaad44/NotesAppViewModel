package com.example.notesappviewmodel.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notesappviewmodel.data.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Note)

    @Query("SELECT * FROM Notes ORDER BY pk ASC")
    fun getNotes(): LiveData<List<Note>>

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}