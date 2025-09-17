package com.k.simplememo.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.k.simplememo.data.local.AppDatabase
import com.k.simplememo.data.local.Note
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NoteDaoTest {
    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getAllNotes_returnsNotesOrderedByCreatedAtDesc() = runTest {
        val noteDao = database.noteDao()
        val oldNote = Note(content = "Old", createdAt = 1L)
        val recentNote = Note(content = "Recent", createdAt = 2L)

        noteDao.insert(oldNote)
        noteDao.insert(recentNote)

        val notes = noteDao.getAllNotes().first()

        assertEquals(listOf("Recent", "Old"), notes.map { it.content })
    }
}
