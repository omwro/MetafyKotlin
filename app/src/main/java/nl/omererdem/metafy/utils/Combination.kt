package nl.omererdem.metafy.utils

import android.util.Log
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.Track
import kotlinx.coroutines.*
import nl.omererdem.metafy.model.Song
import nl.omererdem.metafy.model.SongViewModel
import nl.omererdem.metafy.model.Tag
import nl.omererdem.metafy.model.TagViewModel
import nl.omererdem.metafy.spotifyService

class Combination(var list: ArrayList<String>) {
    suspend fun getPlaylist(tagViewModel: TagViewModel, songViewModel: SongViewModel): List<Song>? =
        withContext(Dispatchers.IO) {
            if (list.size >= 3) {
                val tag1: Tag? = getTagByName(tagViewModel, list[0])
                val symbol: String? = getSymbolByName(list[1])
                val tag2: Tag? = getTagByName(tagViewModel, list[2])

                val savedSongs = songViewModel.getAllSongsOnce()

                var filteredSongs: List<Song>? = null
                if (tag1 != null && symbol != null && tag2 != null) {
                    when (symbol) {
                        "+" -> filteredSongs = getPlusLocalSongs(savedSongs, tag1, tag2)
                        "-" -> filteredSongs = getMinusLocalSongs(savedSongs, tag1, tag2)
                        "=" -> filteredSongs = getEqualLocalSongs(savedSongs, tag1, tag2)
                    }
                    Log.e("Filtered Songs", filteredSongs.toString())
                }

                if (filteredSongs != null) return@withContext filteredSongs
                else return@withContext null
            }
            return@withContext null
        }

    fun getCombinationString(): String {
        var combination = ""
        for (item in list) {
            combination += item
        }
        return combination
    }

    private fun getTagByName(tagViewModel: TagViewModel, name: String): Tag? {
        return tagViewModel.getTagByName(name)
    }

    private fun getSymbolByName(string: String): String? {
        return if (string == "+" || string == "-" || string == "=") {
            string
        } else null
    }

    private fun getPlusLocalSongs(localSongs: List<Song>, tag1: Tag, tag2: Tag): List<Song> {
        return localSongs.filter { song ->
            song.tags?.any { it.equals(tag1) || it.equals(tag2) } == true
        }
    }

    private fun getMinusLocalSongs(localSongs: List<Song>, tag1: Tag, tag2: Tag): List<Song> {
        return localSongs.filter { song ->
            song.tags?.any { it.equals(tag1) && !it.equals(tag2) } == true
        }
    }

    private fun getEqualLocalSongs(localSongs: List<Song>, tag1: Tag, tag2: Tag): List<Song> {
        return localSongs.filter { song ->
            song.tags?.any { it.equals(tag1) && it.equals(tag2) } == true
        }
    }
}