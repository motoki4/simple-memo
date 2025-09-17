package com.k.simplememo

import android.app.Application
import com.k.simplememo.data.NoteRepository
import com.k.simplememo.data.local.AppDatabase

class SimpleMemoApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val repository: NoteRepository by lazy { NoteRepository(database.noteDao()) }
}
