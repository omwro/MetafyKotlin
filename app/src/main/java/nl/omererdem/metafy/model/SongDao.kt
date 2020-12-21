package nl.omererdem.metafy.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SongDao {
    @Query("SELECT * FROM songTable")
    fun getAllSong(): LiveData<List<Song>>

    @Query("SELECT * FROM songTable")
    fun getAllSongOnce(): List<Song>

    @Query("SELECT * FROM songTable WHERE songId = :songId")
    fun getSongById(songId: String): LiveData<Song>

    @Query("SELECT * FROM songTable WHERE songId = :songId")
    fun getSongByIdOnce(songId: String): Song?

    @Insert
    fun insertSong(song: Song)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateSong(song: Song)

    @Delete
    suspend fun deleteSong(song: Song)
}