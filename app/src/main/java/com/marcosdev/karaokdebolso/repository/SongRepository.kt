package com.marcosdev.karaokdebolso.repository

import androidx.lifecycle.LiveData
import com.marcosdev.karaokdebolso.database.SongDao
import com.marcosdev.karaokdebolso.model.Song

class SongRepository(private val songDao: SongDao) {
    val allSongs: LiveData<List<Song>> = songDao.getAllSongs()

    suspend fun insert(song: Song) {
        songDao.insert(song)
    }

    suspend fun update(song: Song) {
        songDao.update(song)
    }

    suspend fun delete(song: Song) {
        songDao.delete(song)
    }
}
