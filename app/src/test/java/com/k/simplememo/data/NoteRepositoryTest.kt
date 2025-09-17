package com.k.simplememo.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.k.simplememo.data.local.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NoteRepositoryTest {
    private lateinit var database: AppDatabase
    private lateinit var repository: NoteRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        repository = NoteRepository(database.noteDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun addNote_insertsNewNote() = runTest {
        repository.addNote("Test note")

        val notes = repository.notes.first()
        assertEquals(1, notes.size)
        assertEquals("Test note", notes.first().content)
    }
}
