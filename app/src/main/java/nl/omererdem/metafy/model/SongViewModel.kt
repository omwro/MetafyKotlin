package nl.omererdem.metafy.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.omererdem.metafy.room.SongRepository

class SongViewModel(application: Application) : AndroidViewModel(application) {
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val repository = SongRepository(application.applicationContext)

    fun getAllSongs(): LiveData<List<Song>> {
        return repository.getAllSongs()
    }

    fun getAllSongsOnce(): List<Song> {
        return repository.getAllSongsOnce()
    }

    fun getSongById(songId: String): LiveData<Song> {
        return repository.getSongById(songId)
    }

    fun getSongByIdOnce(songId: String): Song? {
        return repository.getSongByIdOnce(songId)
    }

    fun insertSong(song: Song) {
        ioScope.launch { repository.insertSong(song) }
    }

    fun updateSong(song: Song) {
        ioScope.launch { repository.updateSong(song) }
    }

    suspend fun deleteSong(song: Song) {
        ioScope.launch { repository.deleteSong(song) }
    }
}