package nl.omererdem.metafy.room

import android.content.Context
import androidx.lifecycle.LiveData
import nl.omererdem.metafy.model.Song
import nl.omererdem.metafy.model.SongDao

class SongRepository(context: Context) {
    private var songDao: SongDao

    init {
        val roomDatabase = RoomDatabase.getDatabase(context)
        songDao = roomDatabase!!.songDao()
    }

    fun getAllSongs(): LiveData<List<Song>> {
        return songDao.getAllSong()
    }

    fun getAllSongsOnce(): List<Song> {
        return songDao.getAllSongOnce()
    }

    fun getSongById(songId: String): LiveData<Song> {
        return songDao.getSongById(songId)
    }

    fun getSongByIdOnce(songId: String): Song? {
        return songDao.getSongByIdOnce(songId)
    }

    fun insertSong(song: Song) {
        songDao.insertSong(song)
    }

    fun updateSong(song: Song) {
        songDao.updateSong(song)
    }

    suspend fun deleteSong(song: Song) {
        songDao.deleteSong(song)
    }
}