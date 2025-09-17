package com.k.simplememo.data

import com.k.simplememo.data.local.Note
import com.k.simplememo.data.local.NoteDao
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    val notes: Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun addNote(content: String) {
        if (content.isBlank()) return
        val note = Note(content = content.trim(), createdAt = System.currentTimeMillis())
        noteDao.insert(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.update(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.delete(note)
    }
}
