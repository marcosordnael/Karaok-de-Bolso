package com.marcosdev.karaokdebolso.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.marcosdev.karaokdebolso.model.Song

@Dao
interface SongDao {
    @Query("SELECT * FROM songs ORDER BY id DESC")
    fun getAllSongs(): LiveData<List<Song>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(song: Song)

    @Update
    suspend fun update(song: Song)

    @Delete
    suspend fun delete(song: Song)
}
