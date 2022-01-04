package com.example.notesappviewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.notesappviewmodel.data.Note
import com.example.notesappviewmodel.data.NoteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NoteViewModel(application: Application): AndroidViewModel(application) {
    private val notesDao = NoteDatabase.getDatabase(application).noteDao()
    private val notes: LiveData<List<Note>>

    init {
        notes = notesDao.getNotes()       
    }

    fun fetchNotes(): LiveData<List<Note>> {
        return notes
    }

    fun addNote(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            notesDao.insertNote(Note(0,text))
        }
    }

    fun editNote(id: Int, text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            notesDao.updateNote(Note(id, text))
        }
    }

    fun deleteNote(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            notesDao.deleteNote(Note(id, ""))
        }
    }
}