package nl.omererdem.metafy.room

import android.content.Context
import androidx.lifecycle.LiveData
import nl.omererdem.metafy.model.Song
import nl.omererdem.metafy.model.SongDao
import nl.omererdem.metafy.model.Tag
import nl.omererdem.metafy.model.TagDao

class SongRepository(context: Context) {
    private var songDao: SongDao

    init {
        val roomDatabase = RoomDatabase.getDatabase(context)
        songDao = roomDatabase!!.songDao()
    }

    fun getAllSongs(): LiveData<List<Song>> {
        return songDao.getAllSong()
    }

    fun getSongById(songId: String): LiveData<Song> {
        return songDao.getSongById(songId)
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