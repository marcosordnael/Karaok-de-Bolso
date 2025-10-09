package com.marcosdev.karaokdebolso.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.marcosdev.karaokdebolso.database.AppDatabase
import com.marcosdev.karaokdebolso.model.Song
import com.marcosdev.karaokdebolso.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SongViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SongRepository

    init {
        val dao = AppDatabase.getDatabase(application).songDao()
        repository = SongRepository(dao)
    }

    val allSongs = repository.allSongs

    fun insert(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(song)
        }
    }

    fun update(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(song)
        }
    }

    fun delete(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(song)
        }
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SongViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SongViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
