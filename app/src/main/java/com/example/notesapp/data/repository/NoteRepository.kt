package com.example.notesapp.data.repository

import androidx.lifecycle.LiveData
import com.example.notesapp.data.local.Note
import com.example.notesapp.data.local.NoteDao

class NoteRepository(private val dao: NoteDao) {
    val allNotes: LiveData<List<Note>> = dao.getAllNotes()

    suspend fun insert(note: Note) = dao.insertNote(note)

    suspend fun delete(note: Note) = dao.deleteNote(note)

    suspend fun update(note: Note) = dao.updateNote(note)
}